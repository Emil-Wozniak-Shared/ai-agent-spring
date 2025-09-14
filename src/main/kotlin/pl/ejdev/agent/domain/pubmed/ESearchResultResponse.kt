package pl.ejdev.agent.domain.pubmed

import com.fasterxml.jackson.annotation.JsonProperty

@Suppress("SpellCheckingInspection")
data class ESearchResultResponse(
    var header: Header,
    @field:JsonProperty("esearchresult")
    val result: ESearchResult,
) {
    companion object {
        val Empty = ESearchResultResponse(Header(), ESearchResult())
    }
}