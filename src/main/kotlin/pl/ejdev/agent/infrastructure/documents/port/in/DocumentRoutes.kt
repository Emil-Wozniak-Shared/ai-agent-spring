package pl.ejdev.agent.infrastructure.documents.port.`in`

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.RouterFunctionDsl
import pl.ejdev.agent.infrastructure.documents.DocumentHandler

fun RouterFunctionDsl.documentRoutes(
    documentHandler: DocumentHandler,
) {
    ("/documents" and accept(APPLICATION_JSON) and contentType(APPLICATION_JSON)).nest {
        POST("", documentHandler::createMany)
        POST("/search", documentHandler::search)
    }
}