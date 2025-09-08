package pl.ejdev.agent.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)
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
        log.error("Filter exception: $message")
        request.setAttribute("exception", this)
    }

    private fun validate(request: HttpServletRequest) {
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        var jwt: String? = null
        var username: String? = null
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            username = JwtHelper.extractUsername(jwt)
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.userDetailsService.loadUserByUsername(username)
            val isTokenValidated = JwtHelper.validateToken(jwt!!, userDetails!!)
            if (isTokenValidated) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
    }
}