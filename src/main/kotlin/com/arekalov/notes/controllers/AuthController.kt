package com.arekalov.notes.controllers

import com.arekalov.notes.data.entity.UserEntity
import com.arekalov.notes.security.AuthService
import com.arekalov.notes.security.TokenPair
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody body: AuthRequestDto,
    ): UserEntity {
        return authService.register(
            email = body.email,
            password = body.password,
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: AuthRequestDto,
    ): TokenPair {
        return authService.login(
            email = body.email,
            password = body.password,
        )
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest,
    ): TokenPair {
        return authService.refreshToken(
            refreshToken = body.refreshToken,
        )
    }
}

data class AuthRequestDto(
    val email: String,
    val password: String,
)

data class RefreshRequest(
    val refreshToken: String,
)
