package pl.ejdev.agent.infrastructure.user.dto

sealed interface AddUserArticlesResult {
    object Success: AddUserArticlesResult
    class Failure(val message: String): AddUserArticlesResult
}