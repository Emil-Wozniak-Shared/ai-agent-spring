package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.User

sealed interface GetUserResult {
    class Some(val user: User): GetUserResult
    object Empty: GetUserResult
}

