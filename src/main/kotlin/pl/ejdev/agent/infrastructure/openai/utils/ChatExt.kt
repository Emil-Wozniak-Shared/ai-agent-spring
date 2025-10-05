package pl.ejdev.agent.infrastructure.openai.utils

import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionMessage.Role
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest

fun completionMessage(
    content: String,
    role: Role
) = ChatCompletionMessage(
    content, role
)

fun completionRequest(
    messages: List<ChatCompletionMessage>,
    model: OpenAIConstants.Model = OpenAIConstants.Model.GPT_3_5_TURBO,
    temperature: Double = OpenAIConstants.TEMPERATURE,
) = ChatCompletionRequest(
    messages,
    model.text,
    temperature,
    false
)