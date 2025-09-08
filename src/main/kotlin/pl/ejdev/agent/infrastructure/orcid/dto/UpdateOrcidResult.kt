package pl.ejdev.agent.infrastructure.orcid.dto

sealed interface UpdateOrcidResult {
    object Success: UpdateOrcidResult
    data class Failure(val message: String): UpdateOrcidResult
}