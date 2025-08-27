package pl.ejdev.agent.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
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
        log.info("Inside JWT filter")
        try {
            validate(request)
        } catch (e: ExpiredJwtException) {
            log.error("Filter exception: ${e.message}")
            request.setAttribute("exception", e)
        } catch (e: BadCredentialsException) {
            log.error("Filter exception: ${e.message}")
            request.setAttribute("exception", e)
        } catch (e: UnsupportedJwtException) {
            log.error("Filter exception: ${e.message}")
            request.setAttribute("exception", e)
        } catch (e: MalformedJwtException) {
            log.error("Filter exception: ${e.message}")
            request.setAttribute("exception", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun validate(request: HttpServletRequest) {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
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
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
    }
}