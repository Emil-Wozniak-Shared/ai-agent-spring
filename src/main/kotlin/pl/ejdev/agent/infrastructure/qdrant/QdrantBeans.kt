package pl.ejdev.agent.infrastructure.qdrant

import io.qdrant.client.QdrantClient
import io.qdrant.client.QdrantGrpcClient
import io.qdrant.client.grpc.Collections.*
import org.springframework.ai.embedding.TokenCountBatchingStrategy
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.core.env.ConfigurableEnvironment
import pl.ejdev.agent.infrastructure.qdrant.adapter.create.CreateQdrantAdapter
import pl.ejdev.agent.infrastructure.qdrant.adapter.search.SearchQdrantAdapter
import pl.ejdev.agent.infrastructure.qdrant.port.out.CreateQdrantPort
import pl.ejdev.agent.infrastructure.qdrant.port.out.SearchQdrantPort
import pl.ejdev.agent.infrastructure.qdrant.usecase.create.CreateQdrantUseCase
import pl.ejdev.agent.infrastructure.qdrant.usecase.search.SearchQdrantUseCase

fun BeanDefinitionDsl.qdrantBeans() {
    bean<QdrantGrpcClient>(::qdrantGrpcClient)
    bean<QdrantClient>()
    bean<VectorStore> { qdrantVectorStore(env, ref()) }
    bean<SearchQdrantPort> { SearchQdrantAdapter(ref(), ref(), ref()) }
    bean<CreateQdrantPort> { CreateQdrantAdapter(ref()) }
    bean<CreateQdrantUseCase> { CreateQdrantUseCase(ref()) }
    bean<SearchQdrantUseCase> { SearchQdrantUseCase(ref(), ref()) }
}

private fun BeanDefinitionDsl.BeanSupplierContext.qdrantVectorStore(
    env: ConfigurableEnvironment,
    client: QdrantClient
): QdrantVectorStore {
    val collectionName: String = env.getProperty("qdrant.collection-name").let(::requireNotNull)
    val refresh = env.getProperty("qdrant.refresh")?.toBoolean() ?: false
    return qdrantVectorStore(
        client.withCollection(collectionName, refresh),
        collectionName
    )
}

private fun BeanDefinitionDsl.BeanSupplierContext.qdrantVectorStore(
    qdrantClient: QdrantClient,
    collectionName: String
): QdrantVectorStore = QdrantVectorStore
    .builder(qdrantClient, ref())
    .collectionName(collectionName)
    .initializeSchema(false)
    .batchingStrategy(TokenCountBatchingStrategy())
    .build()

private fun QdrantClient.withCollection(collectionName: String, refresh: Boolean) = apply {
    val collectionExists = collectionExistsAsync(collectionName).get()
    if (!collectionExists || refresh) {
//        deleteCollectionAsync(collectionName)
        val collection = CreateCollection.newBuilder()
            .setCollectionName(collectionName)
            .setVectorsConfig(
                VectorsConfig.newBuilder().setParams(
                    VectorParams.newBuilder()
                        .setSize(1536)
                        .setDistance(Distance.Cosine)
                        .build()
                )
            )
            .build()
        this.createCollectionAsync(collection).get()
    }
}

private fun BeanDefinitionDsl.qdrantGrpcClient(): QdrantGrpcClient = QdrantGrpcClient
    .newBuilder(
        env.getProperty("qdrant.host")!!,
        env.getProperty("qdrant.port")!!.toInt(),
        env.getProperty("qdrant.use-tls")?.toBoolean() ?: false
    )
    .build()
