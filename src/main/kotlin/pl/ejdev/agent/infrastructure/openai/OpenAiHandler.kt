package pl.ejdev.agent.infrastructure.openai

import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserQuery
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserResponse
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserResult
import pl.ejdev.agent.infrastructure.openai.usecase.DescribeUserUseCase

class OpenAiHandler(
    private val describeUserUseCase: DescribeUserUseCase
) {

    fun describe(request: ServerRequest): ServerResponse =
        describeUserUseCase.handle(DescribeUserQuery)
            .let {
                when(it) {
                    is DescribeUserResult.Failure -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(it.message)
                    is DescribeUserResult.Success -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(DescribeUserResponse(
                            it.email,
                            it.description
                        ))
                }
            }
}