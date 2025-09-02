package pl.ejdev.agent.infrastructure.user.adapter.all

import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersEvent
import pl.ejdev.agent.infrastructure.user.dto.GetAllUsersResult
import pl.ejdev.agent.infrastructure.user.port.`in`.GetAllUsersPort
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class GetAllUsersAdapter(
    private val userRepository: UserRepository
) : GetAllUsersPort {
    override fun handle(event: GetAllUsersEvent): GetAllUsersResult =
        userRepository.findAll().let {GetAllUsersResult(it)  }
}