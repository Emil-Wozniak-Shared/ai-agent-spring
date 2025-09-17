package pl.ejdev.agent.infrastructure.openai.dto

sealed interface DescribeUserResult {
    class Success(val email: String, val description: String): DescribeUserResult
    class Failure(val message: String): DescribeUserResult
}
