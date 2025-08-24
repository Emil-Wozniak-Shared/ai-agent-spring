package pl.ejdev.agent.security.dto

data class TokenRequest(
    val username: String,
    val password: String
)