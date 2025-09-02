package pl.ejdev.agent.infrastructure.user.usecase

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersEvent
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersQuery
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersResult
import pl.ejdev.agent.infrastructure.user.port.`in`.GetAllUsersPort

class GetAllUsersUseCase(
    private val getAllUsersPort: GetAllUsersPort
): UseCase<GetAllUsersQuery, GetAllUsersResult> {
    override fun handle(query: GetAllUsersQuery): GetAllUsersResult =
        getAllUsersPort.handle(GetAllUsersEvent)
}