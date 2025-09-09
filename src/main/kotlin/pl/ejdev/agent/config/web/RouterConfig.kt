package pl.ejdev.agent.config.web

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

object RouterConfig {
    private val logger = KotlinLogging.logger {}

    fun filter(request: ServerRequest, next: (ServerRequest) -> ServerResponse): ServerResponse {
        logger.info { (">> ${request.method()} ${request.path()} ${request.headers()}") }
        return next(request)
    }
}