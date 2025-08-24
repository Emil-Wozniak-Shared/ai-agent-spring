package pl.ejdev.agent.infrastructure.documents.dto

data class SearchDocumentEvent(
    val query: String,
    val limit: Int = 10,
    val threshold: Double = 0.7
)