package com.juanfa.virtualwallet.infrastructure.http.controllers

import UserResponseDTO
import com.juanfa.virtualwallet.application.commands.CreateUserCommand
import com.juanfa.virtualwallet.application.handlers.CreateUserHandler
import com.juanfa.virtualwallet.infrastructure.http.dto.CreateUserRequestDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(private val createUserHandler: CreateUserHandler) {

    @PostMapping
    fun createUser(@RequestBody requestDTO: CreateUserRequestDTO): ResponseEntity<Any> {
        return try {
            val now = LocalDateTime.now()
            val command = CreateUserCommand(
                id = UUID.randomUUID(),
                name = requestDTO.name,
                password = requestDTO.password,
                created = now,
                update = now
            )

            val user = createUserHandler.handle(command)

            val responseDTO = UserResponseDTO(
                id = user.id,
                name = user.name,
                created = user.created,
                update = user.update
            )

            ResponseEntity(responseDTO, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            val errorResponse = mapOf("error" to (e.message ?: "Invalid input"))
            ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            val errorResponse = mapOf("error" to "An unexpected error occurred")
            ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}