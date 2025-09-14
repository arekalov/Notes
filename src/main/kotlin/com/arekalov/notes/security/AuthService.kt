package com.arekalov.notes.security

import com.arekalov.notes.data.entity.RefreshTokenEntity
import com.arekalov.notes.data.entity.UserEntity
import com.arekalov.notes.data.repository.RefreshTokenRepository
import com.arekalov.notes.data.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class AuthService(
    private val jwtService: JwtService,
    private val hashEncoder: HashEncoder,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    fun register(
        email: String,
        password: String,
    ): UserEntity {
        val user = userRepository.findByEmail(email = email)
        if (user != null) {
            throw ResponseStatusException(HttpStatusCode.valueOf(400), "This email already exists.")
        }

        return userRepository.save<UserEntity>(
            UserEntity(
                email = email,
                hashedPassword = hashEncoder.encodePassword(password),
            )
        )
    }

    fun login(
        email: String,
        password: String,
    ): TokenPair {
        val user = userRepository.findByEmail(email = email)
            ?: throw BadCredentialsException("Invalid credentials.")

        if (!hashEncoder.matchesPassword(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val userId = user.id
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(401), "User id can't be null")
        val newAccessToken = jwtService.generateAccessToken(userId.toString())
        val newRefreshToken = jwtService.generateRefreshToken(userId.toString())

        saveRefreshToken(
            rawRefreshToken = newRefreshToken,
            userId = userId,
        )

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
        )
    }

    @Transactional
    fun refreshToken(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(token = refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val userId = UUID.fromString(jwtService.getUserIdFromToken(token = refreshToken))
        val user = userRepository.findById(userId).getOrNull()
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")

        val userIdNonNull = user.id
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(401), "User id cannot be null.")
        val hashedToken = hashEncoder.encodeToken(refreshToken)
        val deletedCount = refreshTokenRepository.deleteByUserIdAndHashedToken(
            userId = userIdNonNull,
            hashedToken = hashedToken,
        )

        if (deletedCount == 0) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val newAccessToken = jwtService.generateAccessToken(userIdNonNull.toString())
        val newRefreshToken = jwtService.generateRefreshToken(userIdNonNull.toString())

        saveRefreshToken(
            userId = userIdNonNull,
            rawRefreshToken = newRefreshToken,
        )
        return TokenPair(
            refreshToken = newRefreshToken,
            accessToken = newAccessToken,
        )
    }

    private fun saveRefreshToken(
        userId: UUID,
        rawRefreshToken: String,
    ) {
        val hashedToken = hashEncoder.encodeToken(rawRefreshToken)
        val expiry = JwtService.REFRESH_TOKEN_VALIDITY_MS
        val expiresAt = Instant.now().plusMillis(expiry)
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedToken,
            )
        )
    }
}

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)