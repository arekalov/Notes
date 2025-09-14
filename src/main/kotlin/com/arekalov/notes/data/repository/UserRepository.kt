package com.arekalov.notes.data.repository

import com.arekalov.notes.data.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByEmail(email: String): UserEntity?

}