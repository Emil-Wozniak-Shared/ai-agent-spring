package pl.ejdev.agent.infrastructure.user.adapter

import org.springframework.security.core.userdetails.User.builder
import org.springframework.security.crypto.password.PasswordEncoder
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.domain.User
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class UserAuthenticationService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    init {
        builder()
            .username("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build()

        builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("ADMIN")
            .build()
            .let {
               val admin = User(
                    name = it.username,
                   email = "admin@agent.pl",
                    hashPassword = it.password,
                    roles = it.authorities.map { a ->
                        Authority.from(a)
                    }
                )
                userRepository.save(admin)
            }
    }

    fun findAll(): List<User> = userRepository.findAll()

}