package pl.ejdev.agent.infrastructure.user.adapter.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.notInList
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.ejdev.agent.domain.UserDto
import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileEntity
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleEntity
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleTable
import pl.ejdev.agent.infrastructure.user.dao.UserArticleTable
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.infrastructure.user.dao.UserTable
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class UserRepositoryImpl(
    private val database: Database
) : UserRepository {
    private val logger = KotlinLogging.logger { }

    override fun findAll(): List<UserEntity> = transaction(database) {
        addLogger(StdOutSqlLogger)
        UserEntity.all().toList()
    }

    override fun findById(id: Long): UserEntity? = transaction(database) {
        addLogger(StdOutSqlLogger)
        UserEntity.findById(id)
    }

    override fun findBy(name: String): UserEntity? = transaction(database) {
        addLogger(StdOutSqlLogger)
        UserEntity.find { UserTable.name eq name }.firstOrNull()
    }

    override fun save(userDto: UserDto): Long = transaction(database) {
        addLogger(StdOutSqlLogger)
        userDto.takeIf { !existsBy(it.email) }
            ?.let { dto ->
                val newUserEntity = UserEntity.new {
                    name = dto.name
                    firstName = dto.firstName
                    lastName = dto.lastName
                    email = dto.email
                    password = dto.password
                    active = dto.active
                    createdAt = dto.createdAt
                    updatedAt = dto.updatedAt
                    roles = dto.roles
                }
                OrcidProfileEntity.new {
                    this.userEntity = newUserEntity
                    email = newUserEntity.email
                    orcid = null

                }
                newUserEntity.id.value
            }
            ?: ALREADY_EXIST_CODE
    }

    override fun existsById(id: Long): Boolean = findById(id) != null

    override fun addArticles(email: String, articles: List<PubmedArticle>): Unit = transaction(
        db = database
    ) {
        addLogger(StdOutSqlLogger)
        val userId = UserEntity.find { UserTable.email eq email }.single().id
        val articleIds = articles.mapNotNull { it.id }
        val missingArticles = allArticleNotByIds(articleIds).map { it.id.value }

        if (missingArticles.isNotEmpty()) {
            val articlesToPersist = articles.filter { it.id in missingArticles }
            ArticleTable.batchInsert(articlesToPersist) { (id, title, authors, source, pubDate) ->
                this[ArticleTable.id] = id!!
                this[ArticleTable.pubmedId] = id
                this[ArticleTable.title] = title
                this[ArticleTable.authors] = authors
                this[ArticleTable.articleSource] = source
                this[ArticleTable.pubDate] = pubDate
            }
            runCatching {
                UserArticleTable.batchInsert(allArticlesByIds(articleIds)) {
                    this[UserArticleTable.article] = it.id
                    this[UserArticleTable.user] = userId
                }
            }.onFailure { logger.error { "ERROR: $it" } }
        }
    }

    private fun allArticlesByIds(ids: List<String>): List<ArticleEntity> =
        ArticleEntity.find { ArticleTable.pubmedId inList ids }.toList()

    private fun allArticleNotByIds(ids: List<String>): List<ArticleEntity> =
        ArticleEntity.find { ArticleTable.pubmedId notInList ids }.toList()

    private fun existsBy(email: String): Boolean = UserEntity.find { UserTable.email eq email }.singleOrNull() != null

    private companion object {
        const val ALREADY_EXIST_CODE = -1L
    }
}