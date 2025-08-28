package pl.ejdev.agent.infrastructure.pubmed.dto

import pl.ejdev.agent.domain.pubmed.ESearchResult
import pl.ejdev.agent.domain.pubmed.Header

data class ESearchResultResponse(
    var header: Header = Header(),
    val esearchresult: ESearchResult = ESearchResult(),
)