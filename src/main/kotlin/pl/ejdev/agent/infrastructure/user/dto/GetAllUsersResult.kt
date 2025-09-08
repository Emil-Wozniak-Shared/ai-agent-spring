package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.UserDto

data class GetAllUsersResult(
    val users: List<UserDto>
)