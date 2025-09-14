package com.arekalov.notes.data.repository

import com.arekalov.notes.data.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, UUID> {

    fun findByUserIdAndHashedToken(
        userId: UUID,
        hashedToken: String
    ): RefreshTokenEntity?

    @Transactional
    @Modifying
    fun deleteByUserIdAndHashedToken(
        userId: UUID,
        hashedToken: String,
    ): Int
}
