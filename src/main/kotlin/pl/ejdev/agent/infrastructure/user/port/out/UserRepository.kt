package pl.ejdev.agent.infrastructure.user.port.out

import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.domain.UserDto
import pl.ejdev.agent.domain.pubmed.PubmedArticle

interface UserRepository {
    fun findAll(): List<UserEntity>
    fun findById(id: Long): UserEntity?
    fun save(userDto: UserDto): Long
    fun existsById(id: Long): Boolean
    fun findBy(name: String): UserEntity?
    fun addArticles(email: String, articles: List<PubmedArticle>)
}

