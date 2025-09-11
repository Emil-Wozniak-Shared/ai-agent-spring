package pl.ejdev.agent.infrastructure.user.adapter.repository

import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfile
import pl.ejdev.agent.infrastructure.user.dao.User
import pl.ejdev.agent.domain.UserDto
import pl.ejdev.agent.infrastructure.user.dao.UserTable
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class UserRepositoryImpl(
    private val database: Database
) : UserRepository {
    override fun findAll(): List<User> = transaction(database) {
        addLogger(StdOutSqlLogger)
        User.all().toList()
    }

    override fun findById(id: Long): User? = transaction(database) {
        addLogger(StdOutSqlLogger)
        User.findById(id)
    }

    override fun findByName(name: String): User? = transaction(database) {
        addLogger(StdOutSqlLogger)
        User.find { UserTable.name eq name }.firstOrNull()
    }

    override fun save(userDto: UserDto): Long = transaction(database) {
        addLogger(StdOutSqlLogger)
        userDto.takeIf { !existsByName(it.name) }
            ?.let { dto ->
                val newUser = User.new {
                    name = dto.name
                    email = dto.email
                    password = dto.password
                    active = dto.active
                    createdAt = dto.createdAt
                    updatedAt = dto.updatedAt
                    roles = dto.roles.map { role -> role.name }
                }
                OrcidProfile.new {
                    this.user = newUser
                    email = newUser.email
                    orcid = null

                }
                newUser.id.value
            }
            ?: ALREADY_EXIST_CODE

    }

    override fun existsById(id: Long): Boolean = findById(id) != null

    private fun existsByName(name: String): Boolean = User.find { UserTable.name eq name }.singleOrNull() != null

    private companion object {
        const val ALREADY_EXIST_CODE = -1L
    }
}