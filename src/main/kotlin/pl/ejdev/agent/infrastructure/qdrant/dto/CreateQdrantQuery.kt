package pl.ejdev.agent.infrastructure.qdrant.dto

import pl.ejdev.agent.domain.Document

data class CreateQdrantQuery(
    val documents: List<Document>
)