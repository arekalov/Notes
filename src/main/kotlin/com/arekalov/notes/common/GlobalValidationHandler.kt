package com.arekalov.notes.common

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class GlobalValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(e: MethodArgumentNotValidException): ResponseEntity<Map<String,Any>> {
        val errors = e.bindingResult.allErrors.map {
            it.defaultMessage ?: "Invalid value"
        }

        return ResponseEntity
            .status(400)
            .body(
                mapOf(
                    "errors" to errors
                )
            )
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun errorCodeException(e: ResponseStatusException): ResponseEntity<Map<String,Any>> {
        return ResponseEntity
            .status(e.statusCode)
            .body(
                mapOf(
                    "body" to e.body,
                    "message" to e.message,
                )
            )
    }
}