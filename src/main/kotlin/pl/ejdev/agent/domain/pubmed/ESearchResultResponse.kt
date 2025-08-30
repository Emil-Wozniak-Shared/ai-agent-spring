package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

@Suppress("SpellCheckingInspection")
data class ESearchResultResponse(
    var header: Header = Header(),
    @field:JsonProperty("esearchresult")
    val result: ESearchResult = ESearchResult(),
)