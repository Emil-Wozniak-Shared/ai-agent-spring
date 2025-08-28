package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

data class ArticleIds(
    @field:JsonProperty("idtype") var idtype: String? = null,
    @field:JsonProperty("idtypen") var idtypen: Int? = null,
    @field:JsonProperty("value") var value: String? = null
)