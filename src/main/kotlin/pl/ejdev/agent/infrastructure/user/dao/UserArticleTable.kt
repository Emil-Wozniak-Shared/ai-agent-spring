package pl.ejdev.agent.infrastructure.user.dao

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.jdbc.batchInsert
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleEntity
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleTable

object UserArticleTable : LongIdTable("user_article") {
    var value = long("value").autoIncrement()
    var user: Column<EntityID<Long>> = reference("app_user", UserTable)
    var article: Column<EntityID<String>> = reference("article", ArticleTable).uniqueIndex()

    fun insertAll(articles: List<ArticleEntity>, userId: EntityID<Long>) {
        batchInsert(articles) {
            this[article] = it.id
            this[user] = userId
        }
    }
}