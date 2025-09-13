package pl.ejdev.agent.security

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.infrastructure.user.dao.UserTable

class UserDetailsServiceProvider(private val database: Database) : UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails? = transaction(database) {
        UserEntity.find { (UserTable.email eq login) or (UserTable.name eq login) }.singleOrNull()
    }
}