package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

data class History(
    @field:JsonProperty("pubstatus") var pubstatus: String? = null,
    @field:JsonProperty("date") var date: String? = null
)