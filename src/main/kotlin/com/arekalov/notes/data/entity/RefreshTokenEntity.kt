package com.arekalov.notes.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

private const val REFRESH_TOKEN_DB_NAME = "refresh_token"

@Entity
@Table(name = REFRESH_TOKEN_DB_NAME)
data class RefreshTokenEntity(
    @Id
    val userId: UUID = UUID.randomUUID(),

    @Column
    val expiresAt: Instant = Instant.now(),

    @Column
    val createdAt: Instant = Instant.now(),

    @Column(unique = true)
    val hashedToken: String = "",
)
