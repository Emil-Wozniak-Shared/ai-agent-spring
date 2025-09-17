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
        SecurityContextHolder.getContext().userEntity.email
            .let { DescribeUserEvent(email = it, articles = getArticles(it)) }
            .let(describeUserPort::handle)

    private fun getArticles(email: String): List<PubmedArticle> =
        when (val articleResult = getUserArticlesPort.handle(GetUserArticlesEvent(email))) {
            is GetUserArticlesResult.Failure -> listOf()
            is GetUserArticlesResult.Success -> articleResult.articles
        }
}