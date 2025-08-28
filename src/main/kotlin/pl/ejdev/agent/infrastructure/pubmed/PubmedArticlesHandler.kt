package pl.ejdev.agent.infrastructure.pubmed

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesQuery
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesRequest
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchArticlesUseCase

class PubmedArticlesHandler(
    private val searchArticlesUseCase: SearchArticlesUseCase
) {
    fun search(request: ServerRequest): ServerResponse =
        request.body<SearchArticlesRequest>()
            .let { SearchArticlesQuery.from(it) }
            .runCatching { searchArticlesUseCase.handle(this) }
            .map { result -> ServerResponse.ok().body(result) }
            .getOrElse { ServerResponse.badRequest().body(mapOf("error" to "Failed to find articles")) }
}