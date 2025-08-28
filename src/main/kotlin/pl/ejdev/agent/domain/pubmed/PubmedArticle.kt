package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

data class PubmedArticle(
    @field:JsonProperty("Title")
    val title: String,

    @field:JsonProperty("Authors")
    val authors: String,

    @field:JsonProperty("Source")
    val source: String,

    @field:JsonProperty("PubDate")
    val pubDate: String
)