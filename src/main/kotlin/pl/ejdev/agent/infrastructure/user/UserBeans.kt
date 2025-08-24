package pl.ejdev.agent.infrastructure.user

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.user.adapter.UserAuthenticationService
import pl.ejdev.agent.infrastructure.user.port.out.UserDao

fun BeanDefinitionDsl.userBeans() {
    bean<UserHandler>()
    bean<UserService>()
    bean<UserDao> { UserAuthenticationService(ref()) }
}