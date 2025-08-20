package pl.ejdev.agent.infrastructure.openai

import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.context.support.BeanDefinitionDsl

fun BeanDefinitionDsl.openAiBeans() {
    bean<OpenAiApi>(::openAiApi)
    bean<OpenAiEmbeddingModel>()
}

private fun BeanDefinitionDsl.openAiApi(): OpenAiApi =
    OpenAiApi.builder()
        .apiKey(env.getProperty("spring.ai.openai.api-key"))
        .baseUrl(env.getProperty("spring.ai.openai.base-url"))
        .completionsPath(env.getProperty("spring.ai.openai.completions-path"))
        .embeddingsPath(env.getProperty("spring.ai.openai.embeddings-path"))
        .build()