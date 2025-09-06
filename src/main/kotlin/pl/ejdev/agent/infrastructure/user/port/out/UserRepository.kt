package pl.ejdev.agent.infrastructure.user.port.out

import pl.ejdev.agent.domain.User

interface UserRepository {
    fun findAll(): List<User>
    fun findById(id: Long): User?
    fun save(user: User): Long
    fun existsById(id: Long): Boolean
    fun findByName(name: String): User?
}

