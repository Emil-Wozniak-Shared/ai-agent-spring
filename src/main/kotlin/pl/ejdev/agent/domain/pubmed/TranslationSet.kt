package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

data class TranslationSet(
    @field:JsonProperty("from") var from: String? = null,
    @field:JsonProperty("to") var to: String? = null
)