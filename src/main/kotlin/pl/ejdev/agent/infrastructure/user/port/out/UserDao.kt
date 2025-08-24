package pl.ejdev.agent.infrastructure.user.port.out

import pl.ejdev.agent.domain.User

interface UserDao {
    fun findAll(): List<User>
    fun findById(id: Long): User?
    fun save(user: User): User
    fun existsById(id: Long): Boolean
}