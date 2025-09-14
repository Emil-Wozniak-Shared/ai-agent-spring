package pl.ejdev.agent.infrastructure.user.dao

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.utils.DEFAULT_VARCHAR_LENGTH
import pl.ejdev.agent.utils.enumNameArray
import pl.ejdev.agent.utils.localDateTime

object UserTable : LongIdTable(name = "app_user") {
    val email = varchar("email", DEFAULT_VARCHAR_LENGTH).default("")
    val name = varchar("name", DEFAULT_VARCHAR_LENGTH)
    val firstName = varchar("first_name", DEFAULT_VARCHAR_LENGTH).default("")
    val lastName = varchar("last_name", DEFAULT_VARCHAR_LENGTH).default("")
    val password = varchar("password", DEFAULT_VARCHAR_LENGTH)
    val active = bool("active").default(false)
    val createdAt = localDateTime("created_at")
    val updatedAt = localDateTime("updated_at")
    
    val roles = enumNameArray<Authority>("roles", 8)
}

