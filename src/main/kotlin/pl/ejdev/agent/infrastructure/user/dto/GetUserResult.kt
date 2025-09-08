package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.UserDto

sealed interface GetUserResult {
    class Some(val userDto: UserDto): GetUserResult
    object Empty: GetUserResult
}

