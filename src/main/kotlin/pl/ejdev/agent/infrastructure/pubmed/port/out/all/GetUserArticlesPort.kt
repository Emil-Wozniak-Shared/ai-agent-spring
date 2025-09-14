package pl.ejdev.agent.infrastructure.pubmed.port.out.all

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesResult

interface GetUserArticlesPort : Port<GetUserArticlesEvent, GetUserArticlesResult>