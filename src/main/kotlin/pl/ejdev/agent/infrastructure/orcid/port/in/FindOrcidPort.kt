package pl.ejdev.agent.infrastructure.orcid.port.`in`

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.FindOrcidResult

interface FindOrcidPort : Port<FindOrcidEvent, FindOrcidResult>