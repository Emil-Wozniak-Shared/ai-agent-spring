package pl.ejdev.agent

import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.runApplication
import pl.ejdev.agent.config.AppBeansConfig

open class AgentApplication

fun main(args: Array<String>) {
    runApplication<AgentApplication>(*args) {
        webApplicationType = WebApplicationType.SERVLET
        setBannerMode(Banner.Mode.OFF)
        addInitializers(AppBeansConfig.beans)
    }
}