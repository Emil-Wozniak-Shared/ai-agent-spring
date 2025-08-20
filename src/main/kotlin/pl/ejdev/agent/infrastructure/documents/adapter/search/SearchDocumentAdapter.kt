package pl.ejdev.agent.infrastructure.documents.adapter.search

import org.springframework.ai.document.Document
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentResult
import pl.ejdev.agent.infrastructure.documents.usecase.search.SearchDocumentUseCase
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchDocumentAdapter(
    private val searchQdrantPort: SearchQdrantPort,
) : SearchDocumentUseCase {
    override fun handle(query: SearchDocumentQuery): List<SearchDocumentResult> =
        query
            .toEvent()
            .let { searchQdrantPort.handle(it) }
            .documents
            .map { it.toDocumentResult() }

    private fun SearchDocumentQuery.toEvent(): SearchQdrantEvent = SearchQdrantEvent(
        query = query,
        limit = limit,
        threshold = threshold
    )

    private fun Document.toDocumentResult(): SearchDocumentResult = SearchDocumentResult(
        id = id,
        text = text ?: "",
        score = score ?: 0.0,
        metadata = metadata
    )
}