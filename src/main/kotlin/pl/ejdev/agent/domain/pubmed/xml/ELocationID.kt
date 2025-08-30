package pl.ejdev.agent.domain.pubmed.xml

import com.fasterxml.jackson.annotation.JsonProperty

data class ELocationID(
    @field:JsonProperty("_EIdType")
    val eIDType: String? = null,

    @field:JsonProperty("_ValidYN")
    val validYN: String? = null,

    @field:JsonProperty("__text")
    val text: String? = null
)