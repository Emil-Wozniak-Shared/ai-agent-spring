package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class Pmid(
    @field:JsonProperty("_Version")
    val version: String? = null,

    @field:JsonProperty("__text")
    val text: String? = null
)