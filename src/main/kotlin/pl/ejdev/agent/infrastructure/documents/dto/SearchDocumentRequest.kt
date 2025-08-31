package pl.ejdev.agent.infrastructure.documents.dto

data class SearchDocumentRequest(
    val query: String,
    val limit: Int = 10,
    val threshold: Double = 0.7,
    val keywords: List<Keyword> = listOf()
) {
    data class Keyword(
        val key: String,
        val value: String
    )
}