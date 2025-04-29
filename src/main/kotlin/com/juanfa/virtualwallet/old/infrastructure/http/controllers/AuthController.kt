package com.juanfa.virtualwallet.old.infrastructure.http.controllers

import com.juanfa.virtualwallet.old.application.commands.LoginUserCommand
import com.juanfa.virtualwallet.old.application.handlers.CreateLoginUserHandler
import com.juanfa.virtualwallet.old.domain.repository.UserRepository
import com.juanfa.virtualwallet.old.infrastructure.http.dto.LoginRequestDTO
import com.juanfa.virtualwallet.old.infrastructure.http.dto.LoginResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val createLoginUserHandler: CreateLoginUserHandler,
    private val userRepository: UserRepository
) {

    @PostMapping("/login")
    fun login(@RequestBody requestDTO: LoginRequestDTO): ResponseEntity<Any> {
        return try {
            val userOptional = userRepository.findByName(requestDTO.name)

            if (userOptional.isEmpty) {
                return ResponseEntity(
                    LoginResponseDTO(false, "User not found"),
                    HttpStatus.UNAUTHORIZED
                )
            }

            val user = userOptional.get()

            val command = LoginUserCommand(
                userId = user.id,
                password = requestDTO.password
            )

            createLoginUserHandler.handle(command)

            ResponseEntity(
                LoginResponseDTO(true, "Login successful"),
                HttpStatus.OK
            )
        } catch (e: IllegalArgumentException) {
            val errorResponse = if (e.message?.contains("User does not exist") == true) {
                LoginResponseDTO(false, "User not found")
            } else {
                LoginResponseDTO(false, e.message ?: "Invalid credentials")
            }
            ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
        } catch (e: Exception) {
            val errorResponse = LoginResponseDTO(false, "An error occurred")
            ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}