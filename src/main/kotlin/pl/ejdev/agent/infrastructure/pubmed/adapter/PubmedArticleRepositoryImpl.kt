package pl.ejdev.agent.infrastructure.pubmed.adapter

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.notInList
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleEntity
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleTable
import pl.ejdev.agent.infrastructure.pubmed.port.out.repository.PubmedArticleRepository
import pl.ejdev.agent.infrastructure.user.dao.UserArticleEntity
import pl.ejdev.agent.infrastructure.user.dao.UserArticleTable
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.infrastructure.user.dao.UserTable

class PubmedArticleRepositoryImpl(
    private val database: Database
) : PubmedArticleRepository {
    private val logger = KotlinLogging.logger { }

    override fun findAll(email: String): List<ArticleEntity> = transaction(database) {
        addLogger(StdOutSqlLogger)
        val userId = UserEntity.find { UserTable.email eq email }.single()
        UserArticleEntity.find { UserArticleTable.user eq userId.id }.map { it.article }
    }

    override fun addAll(email: String, articles: List<PubmedArticle>): Unit = transaction(db = database) {
        addLogger(StdOutSqlLogger)
        articles.runCatching {
            val articleIds = mapNotNull { it.id }
            allArticleNotByIds(articleIds)
                .map { it.id.value }
                .takeIf { it.isNotEmpty() }
                ?.let { missingArticles ->
                    val userId = UserEntity.find { UserTable.email eq email }.single().id
                    val articlesToPersist = articles.filter { it.id in missingArticles }
                    ArticleTable.batchInsert(articlesToPersist) { (id, title, authors, source, pubDate) ->
                        this[ArticleTable.id] = id!!
                        this[ArticleTable.pubmedId] = id
                        this[ArticleTable.title] = title
                        this[ArticleTable.authors] = authors
                        this[ArticleTable.articleSource] = source
                        this[ArticleTable.pubDate] = pubDate
                    }

                    UserArticleTable.batchInsert(allArticlesByIds(articleIds)) {
                        this[UserArticleTable.article] = it.id
                        this[UserArticleTable.user] = userId
                    }
                }
        }.onFailure { logger.error { "$it" } }
    }

    override fun allArticlesByIds(ids: List<String>): List<ArticleEntity> = transaction(database) {
        addLogger(StdOutSqlLogger)
        ArticleEntity.find { ArticleTable.pubmedId inList ids }.toList()
    }

    override fun allArticleNotByIds(ids: List<String>): List<ArticleEntity> = transaction(database) {
        addLogger(StdOutSqlLogger)
        ArticleEntity.find { ArticleTable.pubmedId notInList ids }.toList()
    }
}