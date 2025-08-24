package pl.ejdev.agent.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Date
import java.util.function.Function
import javax.crypto.spec.SecretKeySpec

object JwtHelper {
    private const val secret = "5JzoMbk6E5qIqHSuBTgeQCARtUsxAkBiHwdjXOSW8kWdXzYmP3X51C0"
    private val decoder = Base64.getDecoder()
    private val validity = Instant.now().plus(60, ChronoUnit.MINUTES)

    fun createToken(claims: MutableMap<String, String>, subject: String): String {
        val expiryDate: Date =
            Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + validity.epochSecond))
        val key = SecretKeySpec(decoder.decode(secret), SignatureAlgorithm.HS256.jcaName)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expiryDate)
            .signWith(key)
            .compact()
    }


    fun extractUsername(bearerToken: String): String = extractClaimBody(bearerToken, Claims::getSubject)

    fun <T> extractClaimBody(
        bearerToken: String,
        claimsResolver: Function<Claims, T>
    ): T {
        val jwsClaims = extractClaims(bearerToken)
        return claimsResolver.apply(jwsClaims.getBody())
    }

    private fun extractClaims(bearerToken: String): Jws<Claims> = Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(bearerToken)

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUsername(token)
        return userName == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(bearerToken: String?): Boolean = extractExpiry(bearerToken)!!.before(Date())

    fun extractExpiry(bearerToken: String?): Date? = extractClaimBody(bearerToken!!) { it.expiration }

}