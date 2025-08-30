package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class PublicationType(
    @field:JsonProperty("_UI")
    val ui: String? = null,

    @field:JsonProperty("__text")
    val text: String? = null
)