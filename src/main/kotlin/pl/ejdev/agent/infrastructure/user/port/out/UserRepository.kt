package pl.ejdev.agent.infrastructure.user.port.out

import pl.ejdev.agent.infrastructure.user.dao.User
import pl.ejdev.agent.domain.UserDto

interface UserRepository {
    fun findAll(): List<User>
    fun findById(id: Long): User?
    fun save(userDto: UserDto): Long
    fun existsById(id: Long): Boolean
    fun findByName(name: String): User?
}

