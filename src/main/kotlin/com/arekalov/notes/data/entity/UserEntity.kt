package com.arekalov.notes.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

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