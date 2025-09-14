package com.arekalov.notes.controllers

import com.arekalov.notes.data.dto.NoteRequestDto
import com.arekalov.notes.data.dto.NoteResponseDto
import com.arekalov.notes.data.dto.toNoteEntity
import com.arekalov.notes.data.dto.toNoteResponse
import com.arekalov.notes.data.entity.NoteEntity
import com.arekalov.notes.data.repository.NoteRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

@Tag(name = "Notes", description = "API for notes management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteRepository: NoteRepository,
) {
    @Operation(summary = "Create note", description = "Creates a new note for authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Note successfully created"),
        ApiResponse(responseCode = "401", description = "User not authenticated"),
        ApiResponse(responseCode = "400", description = "Invalid data")
    ])
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

    @Operation(summary = "Get all notes", description = "Returns all notes of authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Notes list successfully retrieved"),
        ApiResponse(responseCode = "401", description = "User not authenticated")
    ])
    @GetMapping
    fun findNotesByOwnerId(): List<NoteResponseDto> {
        val ownerId = UUID.fromString(SecurityContextHolder.getContext().authentication.principal as String)
        return noteRepository.findByOwnerId(ownerId = ownerId).map { it.toNoteResponse() }
    }

    @Operation(summary = "Delete note", description = "Deletes note by ID (only owner can delete their note)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Note successfully deleted"),
        ApiResponse(responseCode = "401", description = "User not authenticated"),
        ApiResponse(responseCode = "404", description = "Note not found"),
        ApiResponse(responseCode = "403", description = "No permission to delete this note")
    ])
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