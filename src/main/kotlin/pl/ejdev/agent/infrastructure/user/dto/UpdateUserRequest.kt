package pl.ejdev.agent.infrastructure.user.dto

data class UpdateUserRequest(
    val name: String,
    val password: String,
)