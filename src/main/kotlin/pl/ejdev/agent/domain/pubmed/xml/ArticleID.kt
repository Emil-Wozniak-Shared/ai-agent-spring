package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class ArticleID(
    @field:JsonProperty("_IdType")
    val idType: String? = null,

    @field:JsonProperty("__text")
    val text: String? = null
)