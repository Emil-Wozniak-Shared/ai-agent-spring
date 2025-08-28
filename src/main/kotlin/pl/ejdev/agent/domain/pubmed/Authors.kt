package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

data class Authors(
    @field:JsonProperty("name") var name: String? = null,
    @field:JsonProperty("authtype") var authtype: String? = null,
    @field:JsonProperty("clusterid") var clusterid: String? = null
)