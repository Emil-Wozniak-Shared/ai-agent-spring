package pl.ejdev.agent.infrastructure.user.adapter.repository

import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.dao.UserTable
import pl.ejdev.agent.infrastructure.user.mapper.UserMapper
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class UserRepositoryImpl(
    private val database: Database
) : UserRepository {
    override fun findAll(): List<User> = transaction(database) {
        addLogger(StdOutSqlLogger)
        UserTable.selectAll().map { UserMapper.from(it) }
    }

    override fun findById(id: Long): User? = transaction(database) {
        addLogger(StdOutSqlLogger)
        UserTable.selectAll()
            .where { UserTable.id eq id }
            .firstOrNull()
            ?.let { UserMapper.from(it) }
    }

    fun findByName(name: String): User? = transaction(database) {
        addLogger(StdOutSqlLogger)
        UserTable.selectAll()
            .where { UserTable.name eq name }
            .firstOrNull()
            ?.let { UserMapper.from(it) }
    }

    override fun save(user: User): Long = transaction(database) {
        addLogger(StdOutSqlLogger)
        if (findByName(user.name) == null) {
            UserTable.insert {
                it[UserTable.name] = user.name
                it[UserTable.password] = user.password
                it[UserTable.active] = user.active
                it[UserTable.createdAt] = user.createdAt
                it[UserTable.updatedAt] = user.updatedAt
                it[UserTable.roles] = user.roles.map { role -> role.name }
            } get UserTable.id
        } else {
            ALREADY_EXIST_CODE
        }
    }

    override fun existsById(id: Long): Boolean = findById(id) != null

    private companion object {
        const val ALREADY_EXIST_CODE = -1L
    }
}