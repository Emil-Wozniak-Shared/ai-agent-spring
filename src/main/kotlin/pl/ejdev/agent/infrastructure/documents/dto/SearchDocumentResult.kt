package pl.ejdev.agent.infrastructure.documents.dto

data class SearchDocumentResult(
    val id: String,
    val text: String,
    val score: Double,
    val metadata: Map<String, Any>
)