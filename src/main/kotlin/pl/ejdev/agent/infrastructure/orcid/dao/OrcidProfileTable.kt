package pl.ejdev.agent.infrastructure.orcid.dao

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import pl.ejdev.agent.infrastructure.user.dao.UserTable

const val MAX_VARCHAR_LENGTH = 255

object OrcidProfileTable : LongIdTable(name = "orcid_profile") {
    val userId = reference("user_id", UserTable).nullable().uniqueIndex()
    val email = varchar("email", MAX_VARCHAR_LENGTH)
    // orcid id
    val orcid = varchar("orcid", MAX_VARCHAR_LENGTH).nullable()
}

