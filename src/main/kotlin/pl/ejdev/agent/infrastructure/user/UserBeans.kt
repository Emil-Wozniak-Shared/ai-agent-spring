package pl.ejdev.agent.infrastructure.user

import org.springframework.context.support.BeanDefinitionDsl
import pl.ejdev.agent.infrastructure.user.adapter.UserAuthenticationService
import pl.ejdev.agent.infrastructure.user.adapter.all.GetAllUsersAdapter
import pl.ejdev.agent.infrastructure.user.adapter.create.CreateUserAdapter
import pl.ejdev.agent.infrastructure.user.adapter.get.GetUserAdapter
import pl.ejdev.agent.infrastructure.user.port.`in`.CreateUserPort
import pl.ejdev.agent.infrastructure.user.port.`in`.GetAllUsersPort
import pl.ejdev.agent.infrastructure.user.port.`in`.GetUserPort
import pl.ejdev.agent.infrastructure.user.port.out.UserRepository
import pl.ejdev.agent.infrastructure.user.adapter.repository.UserRepositoryImpl
import pl.ejdev.agent.infrastructure.user.usecase.CreateUserUseCase
import pl.ejdev.agent.infrastructure.user.usecase.GetAllUsersUseCase
import pl.ejdev.agent.infrastructure.user.usecase.GetUserUseCase

fun BeanDefinitionDsl.userBeans() {
    bean<UserRepository> { UserRepositoryImpl(ref()) }
    bean<UserHandler>()
    bean<GetUserPort> { GetUserAdapter(ref()) }
    bean<GetAllUsersPort> { GetAllUsersAdapter(ref()) }
    bean<CreateUserPort> { CreateUserAdapter(ref(), ref()) }
    bean<GetUserUseCase>()
    bean<GetAllUsersUseCase>()
    bean<CreateUserUseCase>()
    bean<UserAuthenticationService>()
}