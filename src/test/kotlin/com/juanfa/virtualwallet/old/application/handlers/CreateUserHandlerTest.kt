package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.CreateUserCommand
import com.juanfa.virtualwallet.old.domain.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class CreateUserHandlerTest {

    @Autowired
    lateinit var createUserHandler: CreateUserHandler
    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun testShouldCreateUserWhenDataIsValid() {
        val command = CreateUserCommand(
            id = UUID.randomUUID(),
            name = "Juan",
            password = "password",
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        createUserHandler.handle(command)

        val createUser = userRepository.findById(command.id).orElse(null)

        assertNotNull(createUser)
        assertEquals("Juan", createUser.name)
        assertEquals("password", createUser.password)
    }
    @Test
    fun testShouldThrowExceptionWhenNameIsBlank() {
        val command = CreateUserCommand(
            id = UUID.randomUUID(),
            name = "",
            password = "password",
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        val exception = assertThrows<IllegalArgumentException> {
            createUserHandler.handle(command)
        }
        assertEquals("Name cannot be blank", exception.message)
    }
    @Test
    fun testShouldCreateUserSucces() {
        val id = UUID.randomUUID()
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val command = CreateUserCommand(
            id = id,
            name = "Juan",
            password = "Password",
            created = now,
            update = now
        )
        createUserHandler.handle(command)

        val saveUser = userRepository.findById(id).orElse(null)

        assertNotNull(saveUser)
        assertEquals("Juan", saveUser.name)
        assertEquals("Password", saveUser.password)
        assertEquals(now, saveUser.created.truncatedTo(ChronoUnit.SECONDS))
        assertEquals(now, saveUser.update.truncatedTo(ChronoUnit.SECONDS))
    }
}