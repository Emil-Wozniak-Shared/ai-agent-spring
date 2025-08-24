package pl.ejdev.agent.infrastructure.user.dto

data class LoginUserRequest(
    val username: String,
    val password: String
)