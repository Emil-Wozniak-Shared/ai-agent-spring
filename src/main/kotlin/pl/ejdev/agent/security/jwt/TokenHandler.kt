package pl.ejdev.agent.security.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.principalOrNull
import pl.ejdev.agent.security.dto.TokenRequest

private const val X_TOKEN = "X-TOKEN"

class TokenHandler(
    private val tokenService: TokenService,
    private val logoutHandler: SecurityContextLogoutHandler
) {
    private val log = KotlinLogging.logger {}

    fun create(request: ServerRequest): ServerResponse {
        val principal = request.principalOrNull() as Authentication?
        if (principal != null) {
            log.info { "Already login" }
            logoutHandler.logout(request as HttpServletRequest, noContent(), principal)
        }
        return request.body<TokenRequest>()
            .also { log.info { "Login using credentials: $it" } }
            .let(tokenService::generateToken)
            .let { token ->
                ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .headers { it.set(X_TOKEN, token.token) }
                    .body(token)
            }
    }

    private fun noContent() = ServerResponse.noContent().build() as HttpServletResponse
}