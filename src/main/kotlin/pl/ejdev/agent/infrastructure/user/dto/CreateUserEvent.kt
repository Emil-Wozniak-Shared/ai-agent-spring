package pl.ejdev.agent.infrastructure.user.dto

import pl.ejdev.agent.domain.User

data class CreateUserEvent(val user: User)