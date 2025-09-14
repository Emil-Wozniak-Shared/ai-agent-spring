package pl.ejdev.agent.infrastructure.user.adapter.articles.add

import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.AddUserArticlesResult
import pl.ejdev.agent.infrastructure.user.port.`in`.AddUserArticlesPort
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository

class AddUserArticlesAdapter(
    private val userRepository: UserRepository
) : AddUserArticlesPort {
    override fun handle(event: AddUserArticlesEvent): AddUserArticlesResult = userRepository
        .runCatching { addArticles(event.email, event.articles) }
        .map { AddUserArticlesResult.Success }
        .getOrElse { AddUserArticlesResult.Failure("${it.message}") }
}