package pl.ejdev.agent.security.dto

data class TokenRequest(
    val login: String,
    val password: String
)