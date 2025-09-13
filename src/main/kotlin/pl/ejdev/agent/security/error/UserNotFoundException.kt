package pl.ejdev.agent.security.error

import org.springframework.security.authentication.BadCredentialsException

class UserNotFoundException(
    username: String
): BadCredentialsException(
    "User with name $username was not found"
)
