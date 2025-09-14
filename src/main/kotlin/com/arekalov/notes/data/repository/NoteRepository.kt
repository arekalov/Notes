package com.arekalov.notes.data.repository

import com.arekalov.notes.data.entity.NoteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NoteRepository : JpaRepository<NoteEntity, UUID> {
    fun findByOwnerId(ownerId: UUID): List<NoteEntity>
}