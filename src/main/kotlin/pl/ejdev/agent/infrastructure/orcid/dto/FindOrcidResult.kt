package pl.ejdev.agent.infrastructure.orcid.dto

sealed interface FindOrcidResult {
    data class Success(val email: String, val id: String?): FindOrcidResult
    data class Failure(val message: String): FindOrcidResult
}