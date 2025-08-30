package pl.ejdev.agent.infrastructure.pubmed.port.out.get.articleAbstract

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractEvent
import pl.ejdev.agent.infrastructure.pubmed.dto.GetPubmedArticleAbstractResult

interface GetPubmedArticleAbstractPort : Port<GetPubmedArticleAbstractEvent, GetPubmedArticleAbstractResult>