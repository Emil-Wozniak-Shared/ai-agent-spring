package pl.ejdev.agent.infrastructure.qdrant.adapter.search

import io.github.oshai.kotlinlogging.KotlinLogging
import io.qdrant.client.ConditionFactory.matchText
import io.qdrant.client.QdrantClient
import io.qdrant.client.QueryFactory.nearest
import io.qdrant.client.WithPayloadSelectorFactory.enable
import io.qdrant.client.grpc.Points
import io.qdrant.client.grpc.Points.Condition
import io.qdrant.client.grpc.Points.Filter
import io.qdrant.client.grpc.Points.QueryPoints
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
    private  val collection = env["qdrant.collection-name"]!!

    override fun handle(event: SearchQdrantEvent): SearchQdrantResult {
        val conditions = event.createConditions()
        event.useClient( conditions)
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

    private fun SearchQdrantEvent.useClient(conditions: List<Condition>): Sequence<Map<String, String>> =
        embeddings
             .asSequence()
             .map { buildQueryPoints( it, conditions) }
             .map { client.queryAsync(it).get() }
             .flatten()
             .map(::payloadToMap)

    private fun payloadToMap(point: Points.ScoredPoint): Map<String, String> =
        point.payloadMap.map { it.key to it.value.stringValue }.toMap()

    private fun SearchQdrantEvent.createConditions(): List<Condition> =
        keywords.map { (key, value) -> matchText(key, value) }

    private fun buildQueryPoints(embedding: Embedding, conditions: List<Condition>): QueryPoints = QueryPoints
        .newBuilder()
        .setCollectionName(collection)
        .setQuery(nearest(embedding.output.toList()))
        .setFilter(pointsFilter(conditions))
        .setLimit(10)
        .setWithPayload(enable(true))
        .build()

    private fun pointsFilter(conditions: List<Condition>): Filter =
        Filter
            .newBuilder()
            .addAllShould(conditions)
            .build()
}