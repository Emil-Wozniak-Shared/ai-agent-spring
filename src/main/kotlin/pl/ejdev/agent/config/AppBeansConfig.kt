package pl.ejdev.agent.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans
import org.springframework.core.env.get
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.ejdev.agent.config.web.AppWebMvcConfigurer
import pl.ejdev.agent.domain.Authority.ADMIN
import pl.ejdev.agent.infrastructure.documents.documentBeans
import pl.ejdev.agent.infrastructure.embedding.embeddingBeans
import pl.ejdev.agent.infrastructure.openai.openAiBeans
import pl.ejdev.agent.infrastructure.pubmed.pubmedBeans
import pl.ejdev.agent.infrastructure.qdrant.qdrantBeans
import pl.ejdev.agent.infrastructure.user.userBeans
import pl.ejdev.agent.routes.routes
import pl.ejdev.agent.security.AppAuthenticationManager
import pl.ejdev.agent.security.AppAuthenticationProvider
import pl.ejdev.agent.security.UserDetailsServiceProvider
import pl.ejdev.agent.security.jwt.JwtAuthenticationEntryPoint
import pl.ejdev.agent.security.jwt.JwtFilter
import pl.ejdev.agent.security.jwt.TokenHandler
import pl.ejdev.agent.security.jwt.TokenService

object AppBeansConfig {
    val beans: BeanDefinitionDsl = beans {
        utils()
        webServer()
        dbConfiguration()
        security()
        openAiBeans()
        qdrantBeans()
        documentBeans()
        userBeans()
        pubmedBeans()
        embeddingBeans()
        routes()
    }

    private fun BeanDefinitionDsl.webServer() {
        bean { TomcatServletWebServerFactory(env["server.port"]?.toIntOrNull() ?: 8080) }
        bean<BeanPostProcessorsRegistrar>()
        bean<DispatcherServletAutoConfiguration>()
        bean<WebMvcAutoConfiguration>()
        bean<JacksonAutoConfiguration>()
        bean { jacksonObjectMapper {} }
        bean<WebMvcConfigurer> { AppWebMvcConfigurer() }
    }

    fun BeanDefinitionDsl.security() {
        bean<PasswordEncoder> { PasswordEncoderFactories.createDelegatingPasswordEncoder() }
        bean<UserDetailsService> { UserDetailsServiceProvider(ref()) }
        bean<AuthenticationManager> { AppAuthenticationManager(ref()) }
        bean<AuthenticationProvider> { AppAuthenticationProvider(ref(), ref()) }
        bean<TokenService>()
        bean<TokenHandler>()
        bean<JwtAuthenticationEntryPoint>()
        bean<SecurityAutoConfiguration>()
        bean<UserDetailsServiceAutoConfiguration>()
        bean<SecurityContextLogoutHandler> {
            SecurityContextLogoutHandler()
        }
        bean<SecurityFilterChain> {
            val httpSecurity = ref<HttpSecurity>()
            httpSecurity {
                csrf { disable() }
                sessionManagement { sessionCreationPolicy = STATELESS }
                authorizeHttpRequests {
                    authorize(GET, "/", permitAll)
                    authorize(GET, "/health", permitAll)
                    authorize(GET, "/login", permitAll)
                    authorize(POST, "/api/token", permitAll)
                    authorize("/api/user/**", hasRole(ADMIN.name))
                    authorize("/api/documents/**", authenticated)
                    authorize(anyRequest, authenticated)
                }
                logout {
                    logoutSuccessHandler = HttpStatusReturningLogoutSuccessHandler()
                    logoutSuccessUrl = "/"
                    permitAll = true
                }
                addFilterBefore<UsernamePasswordAuthenticationFilter>(JwtFilter(ref()))
                exceptionHandling { authenticationEntryPoint = ref<JwtAuthenticationEntryPoint>() }
            }
            httpSecurity.build()
        }
    }
    fun BeanDefinitionDsl.utils() {
        bean<XmlParser>()

    }

}

