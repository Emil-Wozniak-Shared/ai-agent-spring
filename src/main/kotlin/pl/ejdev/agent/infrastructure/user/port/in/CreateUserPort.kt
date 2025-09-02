package pl.ejdev.agent.infrastructure.user.port.`in`

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.user.dto.CreateUserEvent
import pl.ejdev.agent.infrastructure.user.dto.CreateUserResult

interface CreateUserPort: Port<CreateUserEvent, CreateUserResult>