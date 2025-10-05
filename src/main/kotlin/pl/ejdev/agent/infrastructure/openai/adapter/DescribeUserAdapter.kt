package pl.ejdev.agent.infrastructure.openai.adapter

import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.Role
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest
import org.springframework.http.ResponseEntity
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserEvent
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserResult
import pl.ejdev.agent.infrastructure.openai.port.`in`.DescribeUserPort
import pl.ejdev.agent.infrastructure.openai.utils.completionMessage
import pl.ejdev.agent.infrastructure.openai.utils.completionRequest

private const val TITLE_SEPARATOR = ", "

class DescribeUserAdapter(
    private val openAiApi: OpenAiApi
) : DescribeUserPort {
    override fun handle(event: DescribeUserEvent): DescribeUserResult = event.articles
        .map { it.title }
        .let(::createMessages)
        .let(::completionRequest)
        .let { openAiApi.chatCompletionEntity(it) }
        .handleResponse(event.email)

    private fun createMessages(articleTitles: List<String>): List<ChatCompletionMessage> =
        listOf(
            completionMessage(
                content = "You are HR specialist and scientist expert.",
                role = Role.SYSTEM
            ),
            completionMessage(
                content = """
              Based on titles below describe the profile of person who wrote them:
              <titles>
              ${articleTitles.joinToString(TITLE_SEPARATOR)}
              </titles>
    
              - text should be in first person.
              - focus on subject of the researches.
              - do not mention about the titles.
                """.trimIndent(),
                role = Role.USER
            ),
        )

    private fun ResponseEntity<OpenAiApi.ChatCompletion>.handleResponse(email: String): DescribeUserResult =
        if (!statusCode.is2xxSuccessful) DescribeUserResult.Failure("Failed to received description from AI assistant")
        else DescribeUserResult.Success(
            email,
            body!!.choices.first().message.content()
        )
}