package pl.ejdev.agent.infrastructure.documents.dto

data class SearchDocumentQuery(
    val query: String,
    val limit: Int = 10,
    val threshold: Double = 0.7,
    val keywords: List<Keyword>
) {
    data class Keyword(
        val key: String,
        val value: String
    ) {
        companion object {
            fun from(keyword: SearchDocumentRequest.Keyword) = Keyword(keyword.key, keyword.value)
        }
    }

    companion object {
        fun from(request: SearchDocumentRequest) =
            request.let { (query: String, limit: Int, threshold: Double, keywords) ->
                SearchDocumentQuery(query, limit, threshold, keywords.map { Keyword.from(it) })
            }
    }
}
