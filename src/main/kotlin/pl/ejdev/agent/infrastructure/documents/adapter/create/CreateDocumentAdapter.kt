package pl.ejdev.agent.infrastructure.documents.adapter.create

import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentQuery
import pl.ejdev.agent.infrastructure.documents.dto.CreateDocumentResult
import pl.ejdev.agent.infrastructure.documents.usecase.create.CreateDocumentUseCase
import pl.ejdev.agent.infrastructure.qdrant.dto.CreateQdrantEvent
import pl.ejdev.agent.infrastructure.qdrant.port.out.CreateQdrantPort

class CreateDocumentAdapter(
    private val createQdrantPort: CreateQdrantPort
) : CreateDocumentUseCase {
    override fun handle(query: CreateDocumentQuery): CreateDocumentResult =
        createQdrantPort
            .handle(CreateQdrantEvent(query.documents))
            .let { CreateDocumentResult }

}