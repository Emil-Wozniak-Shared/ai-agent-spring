package pl.ejdev.agent.infrastructure.pubmed.port.out.search.articles

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.pubmed.dto.ArticleResponse
import pl.ejdev.agent.infrastructure.pubmed.dto.GetArticlesSummariesEvent

interface GetArticlesSummariesPort : Port<GetArticlesSummariesEvent, List<ArticleResponse>>