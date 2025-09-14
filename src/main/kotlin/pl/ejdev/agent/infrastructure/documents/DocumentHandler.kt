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
            .map { ServerResponse.ok().contentType(APPLICATION_JSON).body(it) }
            .getOrElse { ServerResponse.badRequest().body(mapOf("error" to "Failed to add documents")) }

    fun search(request: ServerRequest): ServerResponse =
        request.body<SearchDocumentRequest>()
            .let { SearchDocumentQuery.from(it) }
            .let(searchDocumentUseCase::handle)
            .let { ServerResponse.ok().contentType(APPLICATION_JSON).body(it) }
}