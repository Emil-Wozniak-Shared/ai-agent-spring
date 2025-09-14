package pl.ejdev.agent.infrastructure.user.dao

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import pl.ejdev.agent.infrastructure.pubmed.dao.ArticleEntity

class UserArticleEntity(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<UserArticleEntity>(table = UserArticleTable, entityType = UserArticleEntity::class.java)

    var value by UserArticleTable.value
    var user: UserEntity by UserEntity referencedOn UserArticleTable.user
    var article: ArticleEntity by ArticleEntity referencedOn UserArticleTable.article // use referencedOn for normal references
}