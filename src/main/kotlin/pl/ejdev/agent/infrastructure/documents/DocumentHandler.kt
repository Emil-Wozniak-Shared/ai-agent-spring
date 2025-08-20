package pl.ejdev.agent.infrastructure.documents

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
            .runCatching {
                createDocumentUseCase.handle(this)
                mapOf(
                    "message" to "Documents added successfully",
                    "count" to documents.size
                )
            }
            .map { msg -> ServerResponse.ok().body(msg) }
            .getOrElse { ServerResponse.badRequest().body(mapOf("error" to "Failed to add documents")) }

    fun search(request: ServerRequest): ServerResponse =
        request.body<SearchDocumentRequest>()
            .let { (query: String, limit: Int, threshold: Double) -> SearchDocumentQuery(query, limit, threshold) }
            .let(searchDocumentUseCase::handle)
            .let { results -> ServerResponse.ok().body(results) }
}