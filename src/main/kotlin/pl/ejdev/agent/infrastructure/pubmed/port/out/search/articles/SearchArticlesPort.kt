package pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.pubmed.dto.ESearchResultResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.SearchArticlesEvent

interface SearchArticlesPort : Port<SearchArticlesEvent, ESearchResultResponse>