package pl.ejdev.agent.security.jwt

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.security.dto.TokenRequest

class TokenHandler(
    private val tokenService: TokenService
) {
    fun create(request: ServerRequest): ServerResponse = request.body<TokenRequest>()
        .let(tokenService::generateToken)
        .let { ServerResponse.ok().body(it) }
}