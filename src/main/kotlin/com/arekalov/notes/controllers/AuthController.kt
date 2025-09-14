package com.arekalov.notes.controllers

import com.arekalov.notes.data.entity.UserEntity
import com.arekalov.notes.security.AuthService
import com.arekalov.notes.security.TokenPair
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Authentication", description = "API for user registration, login and token refresh")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @Operation(summary = "Register user", description = "Creates a new user account")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User successfully registered"),
            ApiResponse(responseCode = "400", description = "Invalid data")
        ]
    )
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: AuthRequestDto,
    ): UserEntity {
        return authService.register(
            email = body.email,
            password = body.password,
        )
    }

    @Operation(summary = "User login", description = "User authentication and JWT tokens retrieval")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful authentication"),
            ApiResponse(responseCode = "401", description = "Invalid credentials")
        ]
    )
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody body: AuthRequestDto,
    ): TokenPair {
        return authService.login(
            email = body.email,
            password = body.password,
        )
    }

    @Operation(summary = "Refresh token", description = "Refresh access token using refresh token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
            ApiResponse(responseCode = "401", description = "Invalid refresh token")
        ]
    )
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
    @field:Email(message = "Invalid email.")
    val email: String,

    @field:NotBlank(message = "Password can't be blank.")
    val password: String,
)

data class RefreshRequest(
    val refreshToken: String,
)
