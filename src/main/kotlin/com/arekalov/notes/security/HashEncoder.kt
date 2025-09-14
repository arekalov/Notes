package com.arekalov.notes.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class HashEncoder {
    private val bCrypt = BCryptPasswordEncoder()

    fun encodePassword(raw: String): String = bCrypt.encode(raw)

    fun matchesPassword(raw: String, hashed: String): Boolean = bCrypt.matches(raw, hashed)

    fun encodeToken(raw: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(raw.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}