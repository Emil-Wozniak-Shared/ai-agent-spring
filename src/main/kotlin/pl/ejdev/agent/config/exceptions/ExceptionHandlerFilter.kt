package pl.ejdev.agent.config.exceptions

import io.micrometer.core.instrument.config.validate.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import pl.ejdev.agent.config.exceptions.ExceptionHandlerFilter.ErrorCode.*
import pl.ejdev.agent.security.error.UserNotFoundException
import java.time.LocalDateTime

object ExceptionHandlerFilter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun filter(request: ServerRequest, next: (ServerRequest) -> ServerResponse): ServerResponse = try {
        next(request)
    } catch (e: Exception) {
        e.printStackTrace()
        handleException(e, request)
    }

    private fun errorResponse(code: HttpStatus, error: ErrorCode, message: String, path: String) =
        ServerResponse.status(code)
            .contentType(APPLICATION_JSON)
            .body(ErrorResponse(error.name, message, path))

    private fun handleException(exception: Exception, request: ServerRequest): ServerResponse =
        request.path().let { path ->
            when (exception) {
                is UserNotFoundException -> errorResponse(
                    NOT_FOUND, USER_NOT_FOUND, exception.message ?: "User not found", path,
                )

                is ValidationException -> errorResponse(
                    BAD_REQUEST, VALIDATION_ERROR, exception.message ?: "Validation failed", path,
                )

                is IllegalArgumentException -> errorResponse(
                    BAD_REQUEST, INVALID_REQUEST, exception.message ?: "Invalid request", path
                )

                else -> {
                    // Log the unexpected exception
                    logger.error("Unexpected error: ${exception.javaClass.simpleName}: ${exception.message}")
                    exception.printStackTrace()
                    errorResponse(
                        INTERNAL_SERVER_ERROR, INTERNAL_ERROR, "An unexpected error occurred", path,
                    )
                }
            }
        }

    enum class ErrorCode {
        INTERNAL_ERROR,
        VALIDATION_ERROR,
        INVALID_REQUEST,
        USER_NOT_FOUND
    }

    data class ErrorResponse(
        val error: String,
        val message: String,
        val path: String,
        val timestamp: LocalDateTime = LocalDateTime.now()
    )
}