package com.arekalov.notes.data.dto

import com.arekalov.notes.data.entity.NoteEntity
import java.time.Instant
import java.util.*

data class NoteResponseDto(
    val id: UUID?,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
)

fun NoteEntity.toNoteResponse() = NoteResponseDto(
    id = this.id,
    title = this.title,
    content = this.content,
    color = this.color,
    createdAt = this.createdAt,
)