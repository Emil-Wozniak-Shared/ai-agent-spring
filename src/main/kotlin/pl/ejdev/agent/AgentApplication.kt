package pl.ejdev.agent

import org.springframework.ai.vectorstore.qdrant.autoconfigure.QdrantVectorStoreAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import pl.ejdev.agent.config.AppBeansConfig

@SpringBootApplication(
    exclude = [QdrantVectorStoreAutoConfiguration::class]
)
class AgentApplication

fun main(args: Array<String>) {
    runApplication<AgentApplication>(*args) {
        addInitializers(AppBeansConfig.beans)
    }
}