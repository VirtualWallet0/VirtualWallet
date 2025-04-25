package com.juanfa.virtualwallet.infrastructure.http.controllers

import UserResponseDTO
import com.juanfa.virtualwallet.application.commands.CreateUserCommand
import com.juanfa.virtualwallet.application.handlers.CreateUserHandler
import com.juanfa.virtualwallet.domain.model.User
import com.juanfa.virtualwallet.infrastructure.http.dto.CreateUserRequestDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.*

class UserControllerTest {

    private lateinit var createUserHandler: CreateUserHandler
    private lateinit var userController: UserController

    inline fun <reified T> anyNonNull(): T = any(T::class.java)

    @BeforeEach
    fun setup() {
        createUserHandler = mock(CreateUserHandler::class.java)
        userController = UserController(createUserHandler)
    }

    @Test
    fun `createUser should return created when input is valid`() {
        val requestDTO = CreateUserRequestDTO("testUser", "password123")
        val now = LocalDateTime.now()
        val user = User(UUID.randomUUID(), "testUser", "password123", now, now)

        `when`(createUserHandler.handle(anyNonNull())).thenReturn(user)

        val response = userController.createUser(requestDTO)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        val body = response.body as UserResponseDTO
        assertEquals(user.id, body.id)
        assertEquals("testUser", body.name)
    }

    @Test
    fun `createUser should return bad request when handler throws IllegalArgumentException`() {
        val requestDTO = CreateUserRequestDTO("badUser", "badPassword")

        `when`(createUserHandler.handle(anyNonNull()))
            .thenThrow(IllegalArgumentException("Invalid input data"))

        val response = userController.createUser(requestDTO)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val body = response.body as Map<*, *>
        assertEquals("Invalid input data", body["error"])
    }

    @Test
    fun `createUser should return internal server error when unexpected exception occurs`() {
        val requestDTO = CreateUserRequestDTO("testUser", "password123")

        `when`(createUserHandler.handle(anyNonNull()))
            .thenThrow(RuntimeException("Something went wrong"))

        val response = userController.createUser(requestDTO)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        val body = response.body as Map<*, *>
        assertEquals("An unexpected error occurred", body["error"])
    }
}