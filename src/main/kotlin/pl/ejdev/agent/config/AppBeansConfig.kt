package pl.ejdev.agent.config

import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
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
        autoConfigurationBeans()
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

    private fun BeanDefinitionDsl.autoConfigurationBeans() {
        bean { TomcatServletWebServerFactory() }
        bean<ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar>()
        bean<DispatcherServletAutoConfiguration>()
        bean<WebMvcAutoConfiguration>()
        bean<JacksonAutoConfiguration>()
    }
}
