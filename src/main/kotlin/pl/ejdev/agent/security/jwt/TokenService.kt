package pl.ejdev.agent.security.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.security.dto.TokenRequest
import pl.ejdev.agent.security.dto.TokenResponse
import pl.ejdev.agent.security.error.UserNotFoundException

class TokenService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtHelper: JwtHelper
) {
    fun generateToken(tokenRequest: TokenRequest): TokenResponse =
        userDetailsService.loadUserByUsername(tokenRequest.login)
            ?.let { it as UserEntity }
            ?.also { authenticate(tokenRequest) }
            ?.let { jwtHelper.createToken(it.toClaims(), it.email) }
            ?.let { TokenResponse(it) }
            ?: throw UserNotFoundException(tokenRequest.login)

    private fun authenticate(tokenRequest: TokenRequest) = apply {
        UsernamePasswordAuthenticationToken(tokenRequest.login, tokenRequest.password)
            .let(authenticationManager::authenticate)
    }

    private fun UserEntity.toClaims(): MutableMap<String, String> = mutableMapOf(
        "email" to email,
        "roles" to authorities.joinToString(",")
    )
}