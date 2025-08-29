package pl.ejdev.agent.infrastructure.pubmed

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesQuery
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesRequest
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchSummarizeArticlesQuery
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchArticlesUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchSummarizeArticlesUseCase
import kotlin.jvm.optionals.getOrElse

class PubmedArticlesHandler(
    private val searchSummarizeArticlesUseCase: SearchSummarizeArticlesUseCase,
    private val searchSummarizeUseCase: SearchArticlesUseCase,
) {
    fun search(request: ServerRequest): ServerResponse =
        request.body<SearchArticlesRequest>()
            .let { SearchSummarizeArticlesQuery.from(it) }
            .runCatching { searchSummarizeArticlesUseCase.handle(this) }
            .map { ServerResponse.ok().body(it) }
            .getOrElse { ServerResponse.badRequest().body(mapOf("error" to it.message)) }

    fun searchBy(request: ServerRequest): ServerResponse = searchSummarizeUseCase
        .runCatching { handle(SearchArticlesQuery.from(request)) }
        .map { ServerResponse.ok().body(it) }
        .getOrElse { ServerResponse.badRequest().body(mapOf("error" to it.message)) }
}