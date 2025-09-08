package pl.ejdev.agent.infrastructure.user.adapter.get

import pl.ejdev.agent.domain.UserDto
import pl.ejdev.agent.infrastructure.user.dto.GetUserEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserResult
import pl.ejdev.agent.infrastructure.user.port.`in`.GetUserPort
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class GetUserAdapter(
    private val userRepository: UserRepository
) : GetUserPort {
    override fun handle(event: GetUserEvent): GetUserResult =
        userRepository.findById(event.id)
            ?.let { UserDto.from(it) }
            ?.let { GetUserResult.Some(it) }
            ?: GetUserResult.Empty
}