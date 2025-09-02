package pl.ejdev.agent.config.web

import org.slf4j.LoggerFactory
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

object RouterConfig {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun filter(
        request: ServerRequest,
        next: (ServerRequest) -> ServerResponse
    ): ServerResponse {
        logger.info(">> ${request.method()} ${request.path()} ${request.headers()}")
        return next(request)
    }
}