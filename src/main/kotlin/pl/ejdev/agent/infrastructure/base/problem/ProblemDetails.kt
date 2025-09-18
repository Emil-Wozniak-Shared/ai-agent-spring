package pl.ejdev.agent.infrastructure.base.problem

import org.springframework.http.HttpStatus
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

data class ProblemDetails(
    val type: String, // URI reference
    val instance: String,
    val title: String, // summary
    val status: Int, // code
    val detail: String?, // explanation
)

fun problemDetails(
    request: ServerRequest,
    reason: String,
    detail: String? = null
): ServerResponse = ServerResponse
    .status(500)
    .body(
        ProblemDetails(
            type = request.uri().toASCIIString(),
            instance = request.path(),
            title = reason,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            detail = detail
        )
    )