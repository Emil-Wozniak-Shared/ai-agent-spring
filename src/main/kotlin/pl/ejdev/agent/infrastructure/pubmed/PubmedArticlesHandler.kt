package pl.ejdev.agent.infrastructure.pubmed

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractQuery
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesQuery
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesRequest
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchSummarizeArticlesQuery
import pl.ejdev.agent.infrastructure.pubmed.usecase.GetPubmedArticleAbstractUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchArticlesUseCase
import pl.ejdev.agent.infrastructure.pubmed.usecase.SearchSummarizeArticlesUseCase

class PubmedArticlesHandler(
    private val searchSummarizeArticlesUseCase: SearchSummarizeArticlesUseCase,
    private val searchSummarizeUseCase: SearchArticlesUseCase,
    private val getPubmedArticleAbstractUseCase: GetPubmedArticleAbstractUseCase,
) {
    fun search(request: ServerRequest): ServerResponse =
        request.body<SearchArticlesRequest>()
            .let { SearchSummarizeArticlesQuery.from(it) }
            .runCatching { searchSummarizeArticlesUseCase.handle(this) }
            .map { ServerResponse.ok().contentType(APPLICATION_JSON).body(it) }
            .getOrElse { badRequest(it) }

    fun searchBy(request: ServerRequest): ServerResponse = searchSummarizeUseCase
        .runCatching { handle(SearchArticlesQuery.from(request)) }
        .map { ServerResponse.ok().contentType(APPLICATION_JSON).body(it) }
        .getOrElse { badRequest(it) }

    fun abstract(request: ServerRequest): ServerResponse = getPubmedArticleAbstractUseCase
        .runCatching { handle(GetPubmedArticleAbstractQuery.from(request)) }
        .map { ServerResponse.ok().contentType(APPLICATION_JSON).body(it) }
        .getOrElse { badRequest(it) }

    private fun badRequest(throwable: Throwable): ServerResponse =
        ServerResponse.badRequest()
            .contentType(APPLICATION_JSON)
            .body(mapOf("error" to throwable.message))
}