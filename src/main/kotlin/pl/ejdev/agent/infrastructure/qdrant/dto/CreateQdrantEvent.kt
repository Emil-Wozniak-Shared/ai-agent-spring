package pl.ejdev.agent.infrastructure.qdrant.dto

import pl.ejdev.agent.domain.Document

data class CreateQdrantEvent(
    val documents: List<Document>
)