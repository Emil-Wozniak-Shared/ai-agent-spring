package pl.ejdev.agent.infrastructure.user.dao

import org.jetbrains.exposed.v1.core.VarCharColumnType
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.JavaLocalDateTimeColumnType

const val MAX_VARCHAR_LENGTH = 255

object UserTable : LongIdTable(name = "app_user") {
    val email = varchar("email", MAX_VARCHAR_LENGTH).default("")
    val name = varchar("name", MAX_VARCHAR_LENGTH)
    val password = varchar("password", MAX_VARCHAR_LENGTH)
    val active = bool("active").default(false)
    val createdAt = registerColumn("created_at", JavaLocalDateTimeColumnType())
    val updatedAt = registerColumn("updated_at", JavaLocalDateTimeColumnType())
    val roles = array("roles", VarCharColumnType())
}

