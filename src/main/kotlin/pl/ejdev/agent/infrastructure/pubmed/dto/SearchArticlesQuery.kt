package pl.ejdev.agent.infrastructure.pubmed.dto

import org.springframework.web.servlet.function.ServerRequest
import kotlin.jvm.optionals.getOrElse

data class SearchArticlesQuery(
    val ids: List<String>,
    val email: String,
) {
    companion object {
        fun from(request: ServerRequest) = SearchArticlesQuery(
            ids = request.pathVariable("ids").split(","),
            email = request.param("email").getOrElse { throw Exception("email is required") }
        )
    }
}