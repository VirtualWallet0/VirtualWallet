package com.juanfa.virtualwallet.old.infrastructure.http.controllers

import com.juanfa.virtualwallet.old.application.handlers.CreateLoginUserHandler
import com.juanfa.virtualwallet.old.domain.model.User
import com.juanfa.virtualwallet.old.domain.repository.UserRepository
import com.juanfa.virtualwallet.old.infrastructure.http.dto.LoginRequestDTO
import com.juanfa.virtualwallet.old.infrastructure.http.dto.LoginResponseDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.*

class AuthControllerTest {

    private lateinit var createLoginUserHandler: CreateLoginUserHandler
    private lateinit var userRepository: UserRepository
    private lateinit var authController: AuthController

    inline fun <reified T> anyNonNull(): T = Mockito.any(T::class.java)

    @BeforeEach
    fun setup() {
        createLoginUserHandler = mock(CreateLoginUserHandler::class.java)
        userRepository = mock(UserRepository::class.java)
        authController = AuthController(createLoginUserHandler, userRepository)
    }

    @Test
    fun `login should return success when credentials are valid`() {
        val requestDTO = LoginRequestDTO("testUser", "password123")
        val user = User(
            id = UUID.randomUUID(),
            name = "testUser",
            password = "password123",
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        `when`(userRepository.findByName("testUser")).thenReturn(Optional.of(user))
        doNothing().`when`(createLoginUserHandler).handle(anyNonNull())

        val response = authController.login(requestDTO)

        assertEquals(HttpStatus.OK, response.statusCode)
        val body = response.body as LoginResponseDTO
        assertTrue(body.success)
        assertEquals("Login successful", body.message)
    }

    @Test
    fun `login should return unauthorized when user not found`() {
        val requestDTO = LoginRequestDTO("nonExistentUser", "password123")

        `when`(userRepository.findByName("nonExistentUser")).thenReturn(Optional.empty())

        val response = authController.login(requestDTO)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        val body = response.body as LoginResponseDTO
        assertFalse(body.success)
        assertEquals("User not found", body.message)
    }

    @Test
    fun `login should return unauthorized when password is incorrect`() {
        val requestDTO = LoginRequestDTO("testUser", "wrongPassword")
        val user = User(
            id = UUID.randomUUID(),
            name = "testUser",
            password = "password123",
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        `when`(userRepository.findByName("testUser")).thenReturn(Optional.of(user))
        doThrow(IllegalArgumentException("Incorrect username or password"))
            .`when`(createLoginUserHandler).handle(anyNonNull())

        val response = authController.login(requestDTO)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
        val body = response.body as LoginResponseDTO
        assertFalse(body.success)
        assertEquals("Incorrect username or password", body.message)
    }

    @Test
    fun `login should return internal server error when unexpected exception occurs`() {
        val requestDTO = LoginRequestDTO("testUser", "password123")
        val user = User(
            id = UUID.randomUUID(),
            name = "testUser",
            password = "password123",
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        `when`(userRepository.findByName("testUser")).thenReturn(Optional.of(user))
        doThrow(RuntimeException("Unexpected error"))
            .`when`(createLoginUserHandler).handle(anyNonNull())

        val response = authController.login(requestDTO)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        val body = response.body as LoginResponseDTO
        assertFalse(body.success)
        assertEquals("An error occurred", body.message)
    }
}