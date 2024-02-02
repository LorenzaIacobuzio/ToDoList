package com.todolist.utils.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.todolist.models.Login
import com.todolist.utils.models.areLoginCredentialsPresent
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date

class JwtService(
    private val application: Application
) {
    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")
    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    @Suppress("MagicNumber")
    suspend fun createJwtToken(loginRequest: Login): String? {
        return if (areLoginCredentialsPresent(loginRequest.username, loginRequest.password)) {
            JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", loginRequest.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 300_600_000))
                .sign(Algorithm.HMAC256(secret))
        } else {
            null
        }
    }

    suspend fun customValidator(credential: JWTCredential): JWTPrincipal? {
        return if (audienceMatches(credential)) {
            JWTPrincipal(credential.payload)
        } else {
            null
        }
    }

    private fun audienceMatches(
        credential: JWTCredential,
    ): Boolean =
        credential.payload.audience.contains(audience)

    private fun getConfigProperty(path: String) =
        application.environment.config.property(path).getString()
}
