package pl.ejdev.agent.security

import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class AppAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
): DaoAuthenticationProvider(userDetailsService) {
    init {
        setPasswordEncoder(passwordEncoder)
    }
}