package pl.ejdev.agent.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS256
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.get
import pl.ejdev.agent.infrastructure.user.dao.UserEntity
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Function
import javax.crypto.spec.SecretKeySpec

private typealias BearerToken = String

class JwtHelper(env: ConfigurableEnvironment) {
    private val secret = env["app.jwt.secret"]!!
    private val decoder = Base64.getDecoder()
    private val validity = Instant.now().plus(60, ChronoUnit.MINUTES)
    private val signingKey = SecretKeySpec(decoder.decode(secret), HS256.jcaName)

    fun createToken(claims: MutableMap<String, String>, subject: String): String = Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(  Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + validity.epochSecond)))
        .signWith(signingKey)
        .compact()


    fun extractEmail(bearerToken: String): String = extractClaimBody(bearerToken, Claims::getSubject)

    fun <T> extractClaimBody(bearerToken: String, claimsResolver: Function<Claims, T>): T =
        claimsResolver.apply(bearerToken.extractClaims().getBody())

    fun isValidToken(token: String, userDetails: UserEntity): Boolean =
        extractEmail(token) == userDetails.email && !token.isTokenExpired()

    private fun BearerToken.isTokenExpired(): Boolean = this.extractExpiry()!!.before(Date())

    private fun BearerToken.extractClaims(): Jws<Claims> = Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(this)

    private fun BearerToken.extractExpiry(): Date? = extractClaimBody(this) { it.expiration }

}