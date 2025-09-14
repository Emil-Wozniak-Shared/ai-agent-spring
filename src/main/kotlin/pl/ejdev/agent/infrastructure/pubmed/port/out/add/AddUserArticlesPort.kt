package pl.ejdev.agent.infrastructure.pubmed.port.out.add

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesResult

interface AddUserArticlesPort: Port<AddUserArticlesEvent, AddUserArticlesResult>