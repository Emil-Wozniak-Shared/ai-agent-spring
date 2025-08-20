package pl.ejdev.agent.infrastructure.qdrant.adapter.create

import org.springframework.ai.vectorstore.VectorStore
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantResult
import pl.ejdev.agent.infrastructure.qdrant.port.out.CreateQdrantPort
import org.springframework.ai.document.Document as QuadrantDocument

class CreateQdrantAdapter(
    private val vectorStore: VectorStore
) : CreateQdrantPort {
    override fun handle(event: CreateQdrantEvent): CreateQdrantResult = try {
        event.documents
            .map { QuadrantDocument(it.text, it.metadata) }
            .let {
                vectorStore.add(it)
                CreateQdrantResult.Success(it.size)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        CreateQdrantResult.Failure("${e.message}")
    }
}