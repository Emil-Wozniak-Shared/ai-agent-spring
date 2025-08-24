package pl.ejdev.agent.infrastructure.user.adapter

import org.springframework.security.core.userdetails.User.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.port.out.UserDao

class UserAuthenticationService(
    private val passwordEncoder: PasswordEncoder
): UserDao, UserDetailsService {
    private val users: MutableMap<Long, UserDetails> = mutableMapOf()
    init {
        builder()
            .username("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build()
            .let { users.put(0, it) }

        builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("ADMIN")
            .build()
            .let { users.put(1, it) }

    }
    override fun findAll(): List<User> = users.map { (id, user) ->
        User(
            id,
            user.username,
            hashPassword = "",
            roles = user.authorities.map { Authority.from(it) }
        )
    }

    override fun findById(id: Long): User? = users[id]?.let {
        User(
            id,
            it.username,
            it.password,
            roles = it.authorities.map { Authority.from(it) }
        )
    }

    override fun save(user: User): User = user.apply {
        val next = (users.size + 1).toLong()
        builder()
            .username(user.name)
            .password(passwordEncoder.encode("1234"))
            .roles("USER")
            .build()
            .let { users[next] = it }
    }

    override fun existsById(id: Long): Boolean = users.any { it.key == id }

    override fun loadUserByUsername(username: String): UserDetails? = users
        .values
        .find { it.username == username }
}