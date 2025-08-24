package pl.ejdev.agent.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager

class AppAuthenticationManager(
    private val authenticationProvider: AuthenticationProvider
): ProviderManager(authenticationProvider) {
    init {
        isEraseCredentialsAfterAuthentication = false
    }
}