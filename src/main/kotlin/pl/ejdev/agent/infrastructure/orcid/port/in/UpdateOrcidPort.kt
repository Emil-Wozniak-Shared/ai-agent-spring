package pl.ejdev.agent.infrastructure.orcid.port.`in`

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidEvent
import pl.ejdev.agent.infrastructure.orcid.dto.UpdateOrcidResult

interface UpdateOrcidPort : Port<UpdateOrcidEvent, UpdateOrcidResult>