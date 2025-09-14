package com.arekalov.notes.data.dto

import com.arekalov.notes.data.entity.NoteEntity
import jakarta.validation.constraints.NotBlank
import java.util.*

fun NoteRequestDto.toNoteEntity(
    ownerId: UUID,
) = NoteEntity(
    id = this.id,
    title = this.title,
    content = this.content,
    color = this.color ?: 0L,
    ownerId = ownerId,
)

data class NoteRequestDto(
    @NotBlank
    val id: UUID?,

    val title: String,

    val content: String,

    val color: Long?,
)

