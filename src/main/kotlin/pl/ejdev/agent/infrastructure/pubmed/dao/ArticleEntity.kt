package pl.ejdev.agent.infrastructure.pubmed.dao

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import pl.ejdev.agent.infrastructure.user.dao.UserArticleEntity
import pl.ejdev.agent.infrastructure.user.dao.UserArticleTable
import pl.ejdev.agent.infrastructure.user.dao.UserEntity.Companion.referrersOn

class ArticleEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ArticleEntity>(table = ArticleTable, entityType = ArticleEntity::class.java)

    var pubmedId by ArticleTable.pubmedId
    var title by ArticleTable.title
    var authors by ArticleTable.authors
    var source by ArticleTable.articleSource
    var pubDate by ArticleTable.pubDate
}