package pl.ejdev.agent.infrastructure.user.port.`in`

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.user.dto.GetUserEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserResult

interface GetUserPort: Port<GetUserEvent, GetUserResult>