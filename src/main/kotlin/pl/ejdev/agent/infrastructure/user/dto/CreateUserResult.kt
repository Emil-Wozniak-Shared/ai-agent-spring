package pl.ejdev.agent.infrastructure.user.dto

sealed interface CreateUserResult {
    data class Success(val id: Long): CreateUserResult
    data class Failure(val message: String): CreateUserResult
}