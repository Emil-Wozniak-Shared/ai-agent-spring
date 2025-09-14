package pl.ejdev.agent.infrastructure.user.port.out

import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.domain.UserDto

interface UserRepository {
    fun findAll(): List<UserEntity>
    fun findById(id: Long): UserEntity?
    fun save(userDto: UserDto): Long
    fun existsById(id: Long): Boolean
    fun findBy(name: String): UserEntity?
}

