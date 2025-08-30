package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class ArticleIDList(
    @field:JsonProperty("ArticleId")
    val articleID: List<ArticleID>
)