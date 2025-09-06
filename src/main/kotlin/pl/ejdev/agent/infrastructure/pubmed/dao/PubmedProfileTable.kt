package pl.ejdev.agent.infrastructure.pubmed.dao

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.javatime.JavaLocalDateTimeColumnType

const val MAX_VARCHAR_LENGTH = 255

object PubmedProfileTable : Table(name = "pubmed_profile") {
    val id = long("id").autoIncrement()
    val email = varchar("email", MAX_VARCHAR_LENGTH)
    val orcid = varchar("orcid", MAX_VARCHAR_LENGTH)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

