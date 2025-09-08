package pl.ejdev.agent.infrastructure.user.usecase

import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.user.dto.CreateUserEvent
import pl.ejdev.agent.infrastructure.user.dto.CreateUserQuery
import pl.ejdev.agent.infrastructure.user.dto.CreateUserResult
import pl.ejdev.agent.infrastructure.user.port.`in`.CreateUserPort

class CreateUserUseCase(
    private val createUserPort: CreateUserPort
): UseCase<CreateUserQuery, CreateUserResult> {
    override fun handle(query: CreateUserQuery): CreateUserResult =
        createUserPort.handle(CreateUserEvent(query.userDto))
}