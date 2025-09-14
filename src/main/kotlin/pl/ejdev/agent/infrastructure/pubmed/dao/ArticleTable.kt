package pl.ejdev.agent.infrastructure.pubmed.dao

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import pl.ejdev.agent.utils.DEFAULT_VARCHAR_LENGTH

object ArticleTable : IdTable<String>(name = "article") {
    override var id: Column<EntityID<String>> = varchar("id", DEFAULT_VARCHAR_LENGTH).entityId().uniqueIndex("id")
    override var primaryKey = PrimaryKey(id)

    var pubmedId = varchar("pubmed_id", DEFAULT_VARCHAR_LENGTH)
    var title = varchar("title", DEFAULT_VARCHAR_LENGTH)
    var authors = varchar("authors", DEFAULT_VARCHAR_LENGTH)
    var articleSource = varchar("source", DEFAULT_VARCHAR_LENGTH)
    var pubDate = varchar("pubDate", DEFAULT_VARCHAR_LENGTH)
}

