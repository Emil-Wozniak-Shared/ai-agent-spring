package pl.ejdev.agent.infrastructure.user.port.`in`

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersEvent
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersResult

interface GetAllUsersPort : Port<GetAllUsersEvent, GetAllUsersResult>