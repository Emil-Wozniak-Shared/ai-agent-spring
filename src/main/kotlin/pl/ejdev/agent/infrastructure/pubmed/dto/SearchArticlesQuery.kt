package pl.ejdev.agent.infrastructure.pubmed.dto

data class SearchArticlesQuery(
    val query: String,
    val email: String,
    val maxResults: Int
) {
    companion object {
        fun from(request: SearchArticlesRequest): SearchArticlesQuery = request.run {
            SearchArticlesQuery(query, email, maxResults)
        }
    }
}