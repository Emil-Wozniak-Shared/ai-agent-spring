package pl.ejdev.agent.config

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.router
import pl.ejdev.agent.infrastructure.documents.documentBeans
import pl.ejdev.agent.infrastructure.documents.port.`in`.documentRoutes
import pl.ejdev.agent.infrastructure.embedding.embeddingBeans
import pl.ejdev.agent.infrastructure.openai.openAiBeans
import pl.ejdev.agent.infrastructure.qdrant.qdrantBeans

object AppBeansConfig {
    val beans: BeanDefinitionDsl = beans {
        openAiBeans()
        qdrantBeans()
        documentBeans()
        embeddingBeans()
        bean {
            router {
                filter(RouterConfig::filter)
                "/api/v1".nest {
                    documentRoutes(ref())
                }
            }
        }
    }
}
