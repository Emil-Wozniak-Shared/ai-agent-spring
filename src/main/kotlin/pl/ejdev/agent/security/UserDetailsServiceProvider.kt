package pl.ejdev.agent.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import pl.ejdev.agent.infrastructure.user.adapter.UserAuthenticationService

class UserDetailsServiceProvider(
    private val userAuthenticationService: UserAuthenticationService
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? =
        userAuthenticationService.findAll().find {
            it.username == username
        }
}