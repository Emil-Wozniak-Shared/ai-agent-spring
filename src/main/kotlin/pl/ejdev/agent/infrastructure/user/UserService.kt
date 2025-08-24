package pl.ejdev.agent.infrastructure.user

import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.port.out.UserDao

class UserService(private val userRepository: UserDao) {

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun findById(id: Long): User? {
        return userRepository.findById(id)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }
}