package pl.ejdev.agent.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.principalOrNull
import pl.ejdev.agent.security.dto.TokenRequest

class TokenHandler(
    private val tokenService: TokenService,
    private val logoutHandler: SecurityContextLogoutHandler
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(request: ServerRequest): ServerResponse {
        val principal = request.principalOrNull()
        if (principal != null) {
            log.info("Already login")
            logoutHandler.logout(
                request as HttpServletRequest,
                ServerResponse.noContent().build() as HttpServletResponse,
                principal as Authentication
            )
        }
        return request.body<TokenRequest>()
            .let(tokenService::generateToken)
            .let {
                ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(it)
            }
    }
}