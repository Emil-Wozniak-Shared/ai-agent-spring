package pl.ejdev.agent.infrastructure.openai.dto

data class DescribeUserResponse(
    val email: String,
    val description: String
)
