package pl.ejdev.agent.infrastructure.orcid.port.out.repository

interface OrcidProfileRepository {
    fun update(id: String, email: String)
}