package pl.ejdev.agent.infrastructure.qdrant.adapter.search

import io.github.oshai.kotlinlogging.KotlinLogging
import io.qdrant.client.QdrantClient
import io.qdrant.client.ConditionFactory.matchText
import io.qdrant.client.QueryFactory.nearest
import io.qdrant.client.WithPayloadSelectorFactory.enable
import io.qdrant.client.grpc.Points
import org.springframework.ai.embedding.Embedding
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.SearchQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort

class SearchQdrantAdapter(
    private val vectorStore: VectorStore,
    private val client: QdrantClient,
    private val env: Environment
) : SearchQdrantPort {
    private val logger = KotlinLogging.logger {}

    override fun handle(event: SearchQdrantEvent): SearchQdrantResult {
        event.useClient()
        return SearchRequest.builder()
            .query(event.query)
            .topK(event.limit)
            .withExpression(event)
            .similarityThreshold(event.threshold)
            .build()
            .also { logger.info { "Search Qdrant: $it" } }
            .let(vectorStore::similaritySearch)
            .let(::SearchQdrantResult)
}

    private fun SearchRequest.Builder.withExpression(event: SearchQdrantEvent) = apply {
        val expression = event.keywords.joinToString(" && ") { "${it.key} == '${it.value}'" }
        filterExpression(expression)
    }

    private fun SearchQdrantEvent.useClient() {
        val collection = env["qdrant.collection-name"]!!
        val conditions = keywords.map { (key, value) -> matchText(key, value) }
        embeddings
            .asSequence()
            .map { embedding -> buildQueryPoints(collection, embedding, conditions) }
            .map { client.queryAsync(it).get() }
            .flatten()
            .map { point -> point.payloadMap.map { it.key to it.value.stringValue }.toMap() }
    }

    private fun buildQueryPoints(
        collection: String,
        embedding: Embedding,
        conditions: List<Points.Condition>
    ): Points.QueryPoints = Points.QueryPoints.newBuilder()
        .setCollectionName(collection)
        .setQuery(nearest(embedding.output.map { it }))
        .setFilter(Points.Filter.newBuilder().addAllShould(conditions).build())
        .setLimit(10)
        .setWithPayload(enable(true))
        .build()
}