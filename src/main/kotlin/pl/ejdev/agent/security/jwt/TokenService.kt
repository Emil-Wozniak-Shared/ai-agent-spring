package pl.ejdev.agent.security.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import pl.ejdev.agent.infrastructure.user.adapter.UserAuthenticationService
import pl.ejdev.agent.security.dto.TokenRequest
import pl.ejdev.agent.security.dto.TokenResponse
import pl.ejdev.agent.security.error.UserNotFoundException

class TokenService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserAuthenticationService
) {
    fun generateToken(tokenRequest: TokenRequest): TokenResponse = userDetailsService
        .loadUserByUsername(tokenRequest.username)
        ?.let {
            UsernamePasswordAuthenticationToken(tokenRequest.username, tokenRequest.password)
                .let(authenticationManager::authenticate)
            val claims = mutableMapOf(
                "username" to it.username,
                "roles" to it.authorities.joinToString(",")
            )
            val token = JwtHelper.createToken(claims, it.username)
            TokenResponse(token)
        }
        ?: throw UserNotFoundException(tokenRequest.username)
}