package pl.ejdev.agent.domain

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {
    USER {
        override fun getAuthority(): String = this::name.name
    },
    ADMIN {
        override fun getAuthority(): String = this::name.name
    };

    companion object {
        fun from(authority: GrantedAuthority) =
            when (authority.authority.substringAfter("_")) {
                "USER" -> USER
                "ADMIN" -> ADMIN
                else -> error("Unknown authority $authority")
            }

        fun from(authority: String) =
            when (authority) {
                "USER" -> USER
                "ADMIN" -> ADMIN
                else -> error("Unknown authority $authority")
            }
    }
}