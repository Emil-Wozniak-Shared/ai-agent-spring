package pl.ejdev.agent.security.utils

import org.springframework.security.core.context.SecurityContext
import pl.ejdev.agent.infrastructure.user.dao.UserEntity

val SecurityContext.userEntity: UserEntity
    get() = this.authentication.principal as UserEntity