package pl.ejdev.agent.domain.pubmed

data class PubmedArticle(
    val id: String?,
    val title: String,
    val authors: String,
    val source: String,
    val pubDate: String
)

