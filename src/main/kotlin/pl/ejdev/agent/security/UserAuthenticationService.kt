package pl.ejdev.agent.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import pl.ejdev.agent.domain.Authority
import pl.ejdev.agent.domain.UserDto
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class UserAuthenticationService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    init {
        val admin = UserDto(
            name = "ADMIN",
            firstName = "",
            lastName = "",
            email = "admin@agent.pl",
            password = passwordEncoder.encode("admin"),
            roles = listOf(SimpleGrantedAuthority("ROLE_ADMIN")).map (Authority::from)
        )
        userRepository.save(admin)

    }
}