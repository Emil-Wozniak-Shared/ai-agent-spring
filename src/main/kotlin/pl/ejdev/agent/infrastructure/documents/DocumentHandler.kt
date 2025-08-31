package pl.ejdev.agent.infrastructure.documents

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import pl.ejdev.agent.domain.Document
import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentRequest
import pl.ejdev.agent.infrastructure.documents.usecase.create.CreateDocumentUseCase
import pl.ejdev.agent.infrastructure.documents.usecase.search.SearchDocumentUseCase

class DocumentHandler(
    private val createDocumentUseCase: CreateDocumentUseCase,
    private val searchDocumentUseCase: SearchDocumentUseCase,
) {
    fun createMany(request: ServerRequest): ServerResponse =
        request.body<List<Document>>()
            .let(::CreateDocumentQuery)
            .runCatching { createDocumentUseCase.handle(this) }
            .map { result -> ServerResponse.ok().contentType(APPLICATION_JSON).body(result) }
            .getOrElse { ServerResponse.badRequest().body(mapOf("error" to "Failed to add documents")) }

    fun search(request: ServerRequest): ServerResponse =
        request.body<SearchDocumentRequest>()
            .let { (query: String, limit: Int, threshold: Double, keywords) ->
                SearchDocumentQuery(query, limit, threshold, keywords.map {
                    SearchDocumentQuery.Keyword(it.key, it.value)
                })
            }
            .let(searchDocumentUseCase::handle)
            .let { results -> ServerResponse.ok().contentType(APPLICATION_JSON).body(results) }
}