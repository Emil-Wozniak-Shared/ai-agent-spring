package pl.ejdev.agent.infrastructure.pubmed.adapter

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.notInList
import org.jetbrains.exposed.v1.jdbc.Database
import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleEntity
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleTable
import pl.ejdev.agent.infrastructure.pubmed.port.out.repository.PubmedArticleRepository
import pl.ejdev.agent.infrastructure.user.dao.*
import pl.ejdev.agent.utils.read
import pl.ejdev.agent.utils.transaction

class PubmedArticleRepositoryImpl(
    private val database: Database
) : PubmedArticleRepository {
    private val logger = KotlinLogging.logger { }

    override fun findAll(email: String): List<ArticleEntity> = database.read {
        UserArticleEntity.find { UserArticleTable.user eq getUserId(email) }.map { it.article }
    }

    override fun addAll(email: String, articles: List<PubmedArticle>): Unit = database.transaction {
        runCatching {
            val articleIds = articles.mapNotNull { it.id }
            val allArticleNotByIds = ArticleEntity.find { ArticleTable.pubmedId inList articleIds }.toList().map { it.id.value }
            articleIds.filter { it !in allArticleNotByIds }
                .takeIf { it.isNotEmpty() }
                ?.let { missingArticles ->
                    ArticleTable.insertAll(articlesToPersist = articles.filter { it.id in missingArticles })
                    UserArticleTable.insertAll(articles = allArticlesByIds(ids = articleIds), userId = getUserId(email))
                }
        }.onFailure { logger.error { "$it" } }
    }


    override fun allArticlesByIds(ids: List<String>): List<ArticleEntity> = database.transaction {
        ArticleEntity.find { ArticleTable.pubmedId inList ids }.toList()
    }

    override fun allArticleNotByIds(ids: List<String>): List<ArticleEntity> = database.transaction {
        ArticleEntity.find { ArticleTable.pubmedId inList ids }.toList()
    }

    /**
     * <strong>Requires transaction</strong>
     */
    private fun getUserId(email: String): EntityID<Long> = UserEntity.find { UserTable.email eq email }.single().id
}