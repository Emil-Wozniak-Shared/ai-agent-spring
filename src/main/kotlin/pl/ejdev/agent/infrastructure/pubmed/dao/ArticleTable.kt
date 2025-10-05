package pl.ejdev.agent.infrastructure.pubmed.dao

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.jdbc.batchInsert
import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.utils.DEFAULT_VARCHAR_LENGTH

object ArticleTable : IdTable<String>(name = "article") {
    override var id: Column<EntityID<String>> = varchar("id", DEFAULT_VARCHAR_LENGTH).entityId().uniqueIndex("id")
    override var primaryKey = PrimaryKey(id)

    var pubmedId = varchar("pubmed_id", DEFAULT_VARCHAR_LENGTH)
    var title = varchar("title", DEFAULT_VARCHAR_LENGTH)
    var authors = varchar("authors", DEFAULT_VARCHAR_LENGTH)
    var articleSource = varchar("source", DEFAULT_VARCHAR_LENGTH)
    var pubDate = varchar("pubDate", DEFAULT_VARCHAR_LENGTH)

    fun insertAll(articlesToPersist: List<PubmedArticle>) {
        batchInsert(articlesToPersist) { (articleId, title, authors, source, pubDate) ->
            this[id] = articleId!!
            this[pubmedId] = articleId
            this[ArticleTable.title] = title
            this[ArticleTable.authors] = authors
            this[articleSource] = source
            this[ArticleTable.pubDate] = pubDate
        }
    }
}