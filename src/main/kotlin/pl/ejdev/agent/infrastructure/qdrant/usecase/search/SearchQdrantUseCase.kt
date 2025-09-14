package pl.ejdev.agent.infrastructure.qdrant.usecase.search

import org.springframework.ai.embedding.Embedding
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.embedding.dto.GenerateEmbeddingQuery
import pl.ejdev.agent.infrastructure.embedding.port.`in`.GenerateEmbeddingPort
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantQuery
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchQdrantUseCase(
    private val searchQdrantPort: SearchQdrantPort,
    private val generateEmbeddingPort: GenerateEmbeddingPort
) : UseCase<SearchQdrantQuery, SearchQdrantResult> {
    override fun handle(query: SearchQdrantQuery): SearchQdrantResult {
        val result = generateEmbeddingPort.handle(GenerateEmbeddingQuery(query.query))
        return query.toEvent(result.embeddings)
            .let(searchQdrantPort::handle)
    }

    private fun SearchQdrantQuery.toEvent(embeddings: List<Embedding>) = SearchQdrantEvent(
        embeddings,
        query,
        threshold,
        limit,
        keywords.map { SearchQdrantEvent.Keyword(it.key, it.value) }
    )
}