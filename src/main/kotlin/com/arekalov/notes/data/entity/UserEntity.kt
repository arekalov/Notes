package com.arekalov.notes.data.entity

import jakarta.persistence.*
import java.util.*

private const val USER_DB_NAME = "users"

@Entity
@Table(name = USER_DB_NAME)
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val email: String = "",

    @Column(nullable = false)
    val hashedPassword: String = "",
)