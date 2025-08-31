package pl.ejdev.agent.infrastructure.documents.usecase.search

import org.springframework.ai.document.Document
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.SearchDocumentResult
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchDocumentUseCase(
    private val searchQdrantPort: SearchQdrantPort,
) : UseCase<SearchDocumentQuery, List<SearchDocumentResult>> {
    override fun handle(query: SearchDocumentQuery): List<SearchDocumentResult> = query
        .toEvent()
        .let { searchQdrantPort.handle(it) }
        .documents
        .map { it.toDocumentResult() }

    private fun SearchDocumentQuery.toEvent(): SearchQdrantEvent = SearchQdrantEvent(
        query = query,
        limit = limit,
        threshold = threshold,
        keywords = keywords.map {
            SearchQdrantEvent.Keyword(it.key, it.value)
        }
    )

    private fun Document.toDocumentResult(): SearchDocumentResult = SearchDocumentResult(
        id = id,
        text = text ?: "",
        score = score ?: 0.0,
        metadata = metadata
    )
}