package pl.ejdev.agent.infrastructure.pubmed.error

class NotCurrentUserOrcidError(
   private val orcid: String
): RuntimeException(
    "Orcid ID: '$orcid' does not belongs to the current user!"
)