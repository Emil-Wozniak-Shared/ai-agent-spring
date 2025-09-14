package pl.ejdev.agent.infrastructure.user.dao

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleTable

object UserArticleTable : LongIdTable("user_article") {
    var value = long("value").autoIncrement()
    var user: Column<EntityID<Long>> = reference("app_user", UserTable)
    var article: Column<EntityID<String>> = reference("article", ArticleTable).uniqueIndex()
}

