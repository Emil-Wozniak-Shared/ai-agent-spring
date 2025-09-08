package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.UserDto

data class CreateUserEvent(val userDto: UserDto)