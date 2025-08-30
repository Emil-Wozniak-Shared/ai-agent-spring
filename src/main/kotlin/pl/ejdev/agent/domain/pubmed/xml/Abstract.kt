package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class Abstract(
    @field:JsonProperty("AbstractText")
    val abstractText: String? = null,
)