package pl.ejdev.agent.infrastructure.pubmed.dto

data class SearchSummarizeArticlesQuery(
    val query: String,
    val email: String,
    val maxResults: Int
) {
    companion object {
        fun from(request: SearchArticlesRequest): SearchSummarizeArticlesQuery = request.run {
            SearchSummarizeArticlesQuery(query, email, maxResults)
        }
    }
}