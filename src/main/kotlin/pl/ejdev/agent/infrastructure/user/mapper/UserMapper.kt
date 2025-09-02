package pl.ejdev.agent.infrastructure.user.mapper

import org.jetbrains.exposed.v1.core.ResultRow
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.dao.UserTable

object UserMapper {
    fun from(row: ResultRow): User = User(
        id = row[UserTable.id],
        name = row[UserTable.name],
        hashPassword = row[UserTable.password],
        active = row[UserTable.active],
        createdAt = row[UserTable.createdAt],
        updatedAt = row[UserTable.updatedAt],
        roles = row[UserTable.roles]
            .let { roles -> roles as List<String> }
            .map { role -> Authority.from(role) },
    )
}