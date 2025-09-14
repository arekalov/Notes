package com.arekalov.notes.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID


private const val NOTE_DB_NAME = "note"

@Entity
@Table(name = NOTE_DB_NAME)
data class NoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = UUID.randomUUID(),

    @Column(length = 512, nullable = false)
    val title: String = "",

    @Column
    val content: String = "",

    @Column
    val color: Long = 0L,

    @Column
    val createdAt: Instant = Instant.now(),

    @Column
    val ownerId: UUID? = null,
)