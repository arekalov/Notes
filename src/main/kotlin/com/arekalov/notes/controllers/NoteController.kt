package com.arekalov.notes.controllers

import com.arekalov.notes.data.dto.NoteRequestDto
import com.arekalov.notes.data.dto.NoteResponseDto
import com.arekalov.notes.data.dto.toNoteEntity
import com.arekalov.notes.data.dto.toNoteResponse
import com.arekalov.notes.data.entity.NoteEntity
import com.arekalov.notes.data.repository.NoteRepository
import org.springframework.http.HttpStatusCode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteRepository: NoteRepository,
) {
    @PostMapping
    fun saveNoteByOwnerId(
        @RequestBody body: NoteRequestDto
    ): NoteResponseDto {
        val ownerId = UUID.fromString(SecurityContextHolder.getContext().authentication.principal as String)
        val note = noteRepository.save<NoteEntity>(
            body.toNoteEntity(ownerId)
        )

        return note.toNoteResponse()
    }

    @GetMapping
    fun findNotesByOwnerId(): List<NoteResponseDto> {
        val ownerId = UUID.fromString(SecurityContextHolder.getContext().authentication.principal as String)
        return noteRepository.findByOwnerId(ownerId = ownerId).map { it.toNoteResponse() }
    }

    @DeleteMapping
    fun deleteNoteById(
        @RequestParam(required = true)
        noteId: UUID,
    ) {
        val ownerId = UUID.fromString(SecurityContextHolder.getContext().authentication.principal as String)
        val note = noteRepository.findById(noteId).orElseThrow {
            ResponseStatusException(HttpStatusCode.valueOf(400), "Note not found.")
        }

        if (note.ownerId == ownerId) {
            noteRepository.deleteById(noteId)
        } else {
            throw ResponseStatusException(HttpStatusCode.valueOf(403), "You can delete only yours notes.")
        }
    }
}