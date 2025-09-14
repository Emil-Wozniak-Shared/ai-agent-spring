package pl.ejdev.agent.infrastructure.orcid.port.out.repository

import pl.ejdev.agent.infrastructure.orcid.dao.OrcidProfileEntity

interface OrcidProfileRepository {
    fun find(email: String): OrcidProfileEntity?
    fun update(id: String, email: String)
}