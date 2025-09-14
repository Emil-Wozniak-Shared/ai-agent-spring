package pl.ejdev.agent.infrastructure.orcid.dao

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import pl.ejdev.agent.infrastructure.user.dao.UserTable
import pl.ejdev.agent.utils.DEFAULT_VARCHAR_LENGTH

object OrcidProfileTable : LongIdTable(name = "orcid_profile") {
    val userId = reference("user_id", UserTable).nullable().uniqueIndex()
    val email = varchar("email", DEFAULT_VARCHAR_LENGTH)
    // orcid id
    val orcid = varchar("orcid", DEFAULT_VARCHAR_LENGTH).nullable()
}

