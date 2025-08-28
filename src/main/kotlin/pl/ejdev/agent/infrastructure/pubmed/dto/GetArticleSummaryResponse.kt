package pl.ejdev.agent.infrastructure.pubmed.dto

import pl.ejdev.agent.domain.pubmed.Header

data class GetArticleSummaryResponse(
    val header: Header = Header(),
    val result: Map<String, Any> = mapOf(),
)