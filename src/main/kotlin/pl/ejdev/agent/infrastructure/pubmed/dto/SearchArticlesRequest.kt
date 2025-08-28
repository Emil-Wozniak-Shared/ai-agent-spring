package pl.ejdev.agent.infrastructure.pubmed.dto

data class SearchArticlesRequest(
    val query: String,
    val email: String,
    val maxResults: Int
)