package pl.ejdev.agent.infrastructure.user.dto

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)