package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.CreateLoginHistoryCommand
import com.juanfa.virtualwallet.old.domain.model.User
import com.juanfa.virtualwallet.old.domain.repository.LoginHistoryRepository
import com.juanfa.virtualwallet.old.domain.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


@SpringBootTest
class CreateLoginHistoryHandlerTest {

    @Autowired
    private lateinit var handler: CreateLoginHistoryHandler

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var loginHistoryRepository: LoginHistoryRepository


    @Test
    fun testShouldCreateLoginHistorySuccessfully() {
        val userId = UUID.randomUUID()
        val now = LocalDateTime.now()

        val user = User(
            id = userId,
            name = "jf",
            password = "12345",
            created = now,
            update = now
        )
        userRepository.save(user)

        val command = CreateLoginHistoryCommand(
            id = UUID.randomUUID(),
            user = userId,
            dateTime = now,
            success = true
        )
        handler.handle(command)

        val historyList = loginHistoryRepository.findByUser(userId)
        val result = historyList.find { it.id == command.id}

        assertNotNull(result)
        assertEquals(command.user, result.user)
        assertEquals(command.success, result.success)
        //assertEquals(command.dateTime.truncatedTo(ChronoUnit.SECONDS),result.dateTime.truncatedTo(ChronoUnit.SECONDS))
        assertEquals(command.dateTime?.truncatedTo(ChronoUnit.SECONDS), result.dateTime.truncatedTo(ChronoUnit.SECONDS))

    }
    @Test
    fun testShouldFailIfUserDoesNotExist() {
        val command = CreateLoginHistoryCommand(
            id = UUID.randomUUID(),
            user = UUID.randomUUID(),
            dateTime = LocalDateTime.now(),
            success = false
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("User does not exist", exception.message)
    }
    @Test
    fun testShouldAllowMultipleLoginHistoryEntriesPerUser() {
        val userId = UUID.randomUUID()

        val user = User(
            id = userId,
            name = "hannah",
            created = LocalDateTime.now(),
            update = LocalDateTime.now(),
            password = "soso"
        )
        userRepository.save(user)

        val command1 = CreateLoginHistoryCommand(
            id = UUID.randomUUID(),
            user = userId,
            success = true,
            dateTime = LocalDateTime.now().minusMinutes(10)
        )
        val command2 = CreateLoginHistoryCommand(
            id = UUID.randomUUID(),
            user = userId,
            success = false,
            dateTime = LocalDateTime.now()
        )
        handler.handle(command1)
        handler.handle(command2)

        val history = loginHistoryRepository.findByUser(userId)

        assertEquals(2, history.size)
        assertTrue(history.any {!it.success})
        assertTrue(history.any {it.success})
    }
    @Test
    fun testShouldFailWhenCreatingLoginHistoryWithDuplicatedId() {
        val userId = UUID.randomUUID()
        val loginId = UUID.randomUUID()

        val user = User(
            id = userId,
            name = "Perez",
            created = LocalDateTime.now(),
            update = LocalDateTime.now(),
            password = "abcd"
        )
        userRepository.save(user)

        val command = CreateLoginHistoryCommand(
            id = loginId,
            user = userId,
            success = true
        )
        handler.handle(command)

        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Login History with this ID already exists",exception.message )
    }
    @Test
    fun testShouldAssignCurrentDateTimeWhenCreatingLoginHistory() {
        val userId = UUID.randomUUID()
        val loginId = UUID.randomUUID()

        val user = User(
            id = userId,
            name = "Autodate",
            created = LocalDateTime.now(),
            update = LocalDateTime.now(),
            password = "lolo"
        )
        userRepository.save(user)

        val before  = LocalDateTime.now()

        val command = CreateLoginHistoryCommand(
            id = loginId,
            user = userId,
            success = true
        )
        handler.handle(command)

        val save = loginHistoryRepository.findById(loginId).orElseThrow()
        val after = LocalDateTime.now()

        assertTrue(save.dateTime.isAfter(before) || save.dateTime.isEqual(before))
        assertTrue(save.dateTime.isBefore(after) || save.dateTime.isEqual(after))
    }

}