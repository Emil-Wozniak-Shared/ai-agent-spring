package pl.ejdev.agent.infrastructure.openai.usecase

import org.springframework.security.core.context.SecurityContextHolder
import pl.ejdev.agent.domain.pubmed.PubmedArticle
import pl.ejdev.agent.infrastructure.base.usecase.UseCase
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserEvent
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserQuery
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserResult
import pl.ejdev.agent.infrastructure.openai.port.`in`.DescribeUserPort
import pl.ejdev.agent.infrastructure.pubmed.port.out.all.GetUserArticlesPort
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesEvent
import pl.ejdev.agent.infrastructure.user.dto.GetUserArticlesResult
import pl.ejdev.agent.security.utils.userEntity

class DescribeUserUseCase(
    private val describeUserPort: DescribeUserPort,
    private val getUserArticlesPort: GetUserArticlesPort
) : UseCase<DescribeUserQuery, DescribeUserResult> {
    override fun handle(query: DescribeUserQuery): DescribeUserResult =
        getArticles()
            .let(::DescribeUserEvent)
            .let(describeUserPort::handle)

    private fun getArticles(): List<PubmedArticle> {
        val userEntity = SecurityContextHolder.getContext().userEntity
        val event = GetUserArticlesEvent(userEntity.email)
        return when (val articleResult = getUserArticlesPort.handle(event)) {
            is GetUserArticlesResult.Failure -> listOf()
            is GetUserArticlesResult.Success -> articleResult.articles
        }
    }
}