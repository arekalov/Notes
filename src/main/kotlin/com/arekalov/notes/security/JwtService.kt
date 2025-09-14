package com.arekalov.notes.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

private const val TYPE_CLAIM = "type"
private const val ACCESS_TYPE = "access"
private const val REFRESH_TYPE = "refresh"

const val BEARER_PREFIX = "Bearer "

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
) {
    private val secretKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtSecret))

    private val jwtsParser = Jwts.parser()
        .verifyWith(secretKey)
        .build()

    private fun generateToken(
        userId: String,
        type: String,
        expiry: Long,
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim(TYPE_CLAIM, type)
            .issuedAt(Date())
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(
        userId: String
    ): String = generateToken(
        userId = userId,
        type = ACCESS_TYPE,
        expiry = ACCESS_TOKEN_VALIDITY_MS,
    )

    fun generateRefreshToken(
        userId: String
    ): String = generateToken(
        userId = userId,
        type = REFRESH_TYPE,
        expiry = REFRESH_TOKEN_VALIDITY_MS,
    )

    fun validateAccessToken(token: String): Boolean {
        val claims = parseTokenClaims(token) ?: return false
        val type = claims.get(TYPE_CLAIM) as? String ?: return false
        return type == ACCESS_TYPE
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseTokenClaims(token) ?: return false
        val type = claims.get(TYPE_CLAIM) as? String ?: return false
        return type == REFRESH_TYPE
    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseTokenClaims(token)
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid user token")
        return claims.subject
    }

    private fun parseTokenClaims(token: String): Claims? {
        val rawToken = if (token.startsWith(BEARER_PREFIX)) {
            token.removePrefix(BEARER_PREFIX)
        } else {
            token
        }

        return try {
            jwtsParser
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val ACCESS_TOKEN_VALIDITY_MS = 15 * 60 * 1000L
        const val REFRESH_TOKEN_VALIDITY_MS = 30 * 24 * 60 * 60 * 1000L
    }
}
