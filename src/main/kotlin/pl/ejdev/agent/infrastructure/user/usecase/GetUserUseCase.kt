package pl.ejdev.agent.infrastructure.user.usecase

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.user.dto.GetUserEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserQuery
import pl.ejdev.agent.infrastructure.user.dto.GetUserResult
import pl.ejdev.agent.infrastructure.user.port.`in`.GetUserPort

class GetUserUseCase(
    private val getUserPort: GetUserPort
): UseCase<GetUserQuery, GetUserResult> {
    override fun handle(query: GetUserQuery): GetUserResult =
        getUserPort.handle(GetUserEvent(query.id))
}