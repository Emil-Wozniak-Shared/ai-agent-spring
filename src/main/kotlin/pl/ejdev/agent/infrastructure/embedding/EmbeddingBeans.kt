package pl.ejdev.agent.infrastructure.embedding

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.embedding.adapter.generate.GenerateEmbeddingAdapter
import pl.ejdev.agent.infrastructure.embedding.port.`in`.GenerateEmbeddingPort
import pl.ejdev.agent.infrastructure.embedding.usecase.generate.GenerateEmbeddingUseCase

fun BeanDefinitionDsl.embeddingBeans() {
    bean<GenerateEmbeddingPort> { GenerateEmbeddingAdapter(ref()) }
    bean<GenerateEmbeddingUseCase> { GenerateEmbeddingUseCase(ref()) }
}