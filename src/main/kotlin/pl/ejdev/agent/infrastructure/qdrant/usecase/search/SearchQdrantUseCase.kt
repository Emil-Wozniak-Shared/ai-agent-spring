package pl.ejdev.agent.infrastructure.qdrant.usecase.search

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantQuery
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchQdrantUseCase(
    private val searchQdrantPort: SearchQdrantPort
) : UseCase<SearchQdrantQuery, SearchQdrantResult> {
    override fun handle(query: SearchQdrantQuery): SearchQdrantResult =
        query.toEvent().let(searchQdrantPort::handle)

    private fun SearchQdrantQuery.toEvent() = SearchQdrantEvent(
        queryVector,
        query,
        threshold,
        limit,
    )
}