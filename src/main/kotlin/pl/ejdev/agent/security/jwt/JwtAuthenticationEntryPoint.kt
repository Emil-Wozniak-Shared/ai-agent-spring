package pl.ejdev.agent.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.OutputStream

class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val log = KotlinLogging.logger {}

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val exception = request.getAttribute("exception") as Exception?
        response.status = SC_UNAUTHORIZED
        response.contentType = APPLICATION_JSON_VALUE
        exception?.printStackTrace()
        log.error { "Authentication Exception: $exception " }
        val data: MutableMap<String?, Any?> = HashMap()
        data["message"] = if (exception != null) exception.message else authException.cause.toString()
        val out: OutputStream = response.outputStream
        val mapper = ObjectMapper()
        mapper.writeValue(out, data)
        out.flush()
    }
}