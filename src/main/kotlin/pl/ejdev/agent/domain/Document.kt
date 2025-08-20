package pl.ejdev.agent.domain

import java.util.UUID

data class Document(
    val id: UUID = UUID.randomUUID(),
    val text: String,
    val metadata: Map<String, Any> = emptyMap()
)