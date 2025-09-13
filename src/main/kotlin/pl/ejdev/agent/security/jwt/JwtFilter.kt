package pl.ejdev.agent.security.jwt

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import pl.ejdev.agent.security.error.UserNotFoundException

private const val BEARER = "Bearer "

class JwtFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtHelper: JwtHelper
) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            validate(request)
        } catch (e: ExpiredJwtException) {
            e.logAndAttach(request)
        } catch (e: BadCredentialsException) {
            e.logAndAttach(request)
        } catch (e: UnsupportedJwtException) {
            e.logAndAttach(request)
        } catch (e: MalformedJwtException) {
            e.logAndAttach(request)
        }
        filterChain.doFilter(request, response)
    }

    fun RuntimeException.logAndAttach(request: HttpServletRequest) {
        log.error { "Filter exception: $message" }
        request.setAttribute("exception", this)
    }

    private fun validate(request: HttpServletRequest) {
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        var jwt: String? = null
        var login: String? = null
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            jwt = authorizationHeader.substring(7)
            login = jwtHelper.extractEmail(jwt)
        }

        if (login != null && SecurityContextHolder.getContext().authentication == null && jwt != null) {
            this.userDetailsService.loadUserByUsername(login)
                ?.let { it as UserEntity? }
                ?.takeIf { jwtHelper.isValidToken(jwt, it) }
                ?.toAuthenticationToken(request)
                ?.also { SecurityContextHolder.getContext().authentication = it }
                .run { throw UserNotFoundException(login) }
        }
    }

    private fun UserEntity.toAuthenticationToken(request: HttpServletRequest) =
        UsernamePasswordAuthenticationToken(this, null, this.authorities)
            .withDetails(request)

    private fun UsernamePasswordAuthenticationToken.withDetails(request: HttpServletRequest) = apply {
        details = WebAuthenticationDetailsSource().buildDetails(request)
    }
}