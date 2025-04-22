package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.LoginUserCommand
import com.juanfa.virtualwallet.domain.model.User
import com.juanfa.virtualwallet.infrastructure.db.LoginHistoryRepositoryImpl
import com.juanfa.virtualwallet.infrastructure.db.UserRepositoryImpl
import com.juanfa.virtualwallet.infrastructure.db.LoginHistoryRepositoryJPA
import com.juanfa.virtualwallet.infrastructure.db.UserRepositoryJPA
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@SpringBootTest
class CreateLoginUserHandlerTest {

    @Autowired
    private lateinit var userRepositoryJPA: UserRepositoryJPA

    @Autowired
    private lateinit var loginHistoryRepositoryJPA: LoginHistoryRepositoryJPA

    private lateinit var handler: CreateLoginUserHandler
    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var loginHistoryRepository: LoginHistoryRepositoryImpl

    @BeforeEach
    fun setUp() {
        userRepository = UserRepositoryImpl(userRepositoryJPA)
        loginHistoryRepository = LoginHistoryRepositoryImpl(loginHistoryRepositoryJPA)
        handler = CreateLoginUserHandler(userRepository, loginHistoryRepository)
    }

    @Test
    fun testShouldLoginSuccessfullyAndCreateLoginHistory() {
        val userId = UUID.randomUUID()
        val password = "123456"

        val user = User(
            id = userId,
            name = "Pepe",
            password = password,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        userRepository.save(user)

        val command = LoginUserCommand(
            userId = userId,
            password = password
        )
        handler.handle(command)

        val loginHistories = loginHistoryRepository.findByUser(userId)
        assertEquals(1, loginHistories.size)
        assertTrue(loginHistories[0].success)
        assertEquals(userId, loginHistories[0].user)
    }
    @Test
    fun testShouldThrowExceptionAndCreateFailedLoginHistoryWhenPasswordIsIncorrect() {
        val userId = UUID.randomUUID()
        val correctPassword = "123456"
        val wrongPassword = "wrongpassword"

        val user = User(
            id = userId,
            name = "Pipo",
            password = correctPassword,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        userRepository.save(user)

        val command = LoginUserCommand(
            userId = userId,
            password = wrongPassword
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Incorrect username or password", exception.message)

        val loginHistory =
            loginHistoryRepository.findByUser(userId)
            assertEquals(1, loginHistory.size)
            assertEquals(userId, loginHistory[0].user)
            assertFalse(loginHistory[0].success)
    }
    @Test
    fun testShouldFailLoginWhenUserDoesNotExist() {
        val userId = UUID.randomUUID()

        val command = LoginUserCommand(
            userId = userId,
            password = "wrong"
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("User does not exist", exception.message)
    }
    @Test
    fun testAllowMultipleLoginAttemptsAndRecordHistoryCorrectly() {
        val userId = UUID.randomUUID()
        val password = "mypass"

        val user = User(
            id = userId,
            name = "Pablo",
            password = password,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        userRepository.save(user)

        val correctCommand = LoginUserCommand(
            userId = userId,
            password = password
        )
        val incorrectCommand = LoginUserCommand(
            userId = userId,
            password = "wrongpass"
        )
        handler.handle(correctCommand)

        try {
            handler.handle(incorrectCommand)
        }catch (e: IllegalArgumentException){}

        handler.handle(correctCommand)

        val history =
            loginHistoryRepository.findByUser(userId)
        assertEquals(3, history.size)
        assertEquals(2, history.count{it.success})
        assertEquals(1, history.count{!it.success})
    }
    @Test
    fun testShouldStoreLoginHistoryInChronologicalOrder() {
        val userId = UUID.randomUUID()
        val password = "123pass"

        val user = User(
            id = userId,
            name = "chronotester",
            password = password,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        userRepository.save(user)

        handler.handle(LoginUserCommand(userId, password))

        try {
            handler.handle(LoginUserCommand(userId, "wrongpass"))
        }catch (e: Exception){}

        handler.handle(LoginUserCommand(userId, password))

        val history =
            loginHistoryRepository.findByUser(userId)
        assertEquals(3, history.size)

        val timestamps = history.map {it.dateTime}
        val sorted = timestamps.sorted()

        assertEquals(sorted, timestamps, "Login history entries should be in chronological order")


    }
    @Test
    fun testShouldAssignCurrentDateTimeOnLogin() {
        val userId = UUID.randomUUID()
        val password = "123"

        val user = User(
            id = userId,
            name = "test",
            password = password,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        userRepository.save(user)

        val command = LoginUserCommand(
            userId = userId,
            password = password
        )

        val beforeLogin = LocalDateTime.now()
        handler.handle(command)
        val afterLogin = LocalDateTime.now()

        val loginHistory = loginHistoryRepository.findByUser(userId)
        assertEquals(1, loginHistory.size)

        val loginTime = loginHistory.first().dateTime
        assertTrue(loginTime.isAfter(beforeLogin) || loginTime.isEqual(beforeLogin))
        assertTrue(loginTime.isBefore(afterLogin) || loginTime.isEqual(afterLogin))
    }
    @Test
    fun testShouldRecordDateTimeForFailedLoginAttempt() {
        val userId = UUID.randomUUID()
        val correctPassword = "123correct"
        val wrongPassword = "123wrong"

        val user = User(
            id = userId,
            name = "test",
            password = correctPassword,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        userRepository.save(user)

        val beforeLogin = LocalDateTime.now()
        try {
            val command = LoginUserCommand(
                userId = userId,
                password = wrongPassword
            )
            handler.handle(command)
        }catch (e: IllegalArgumentException){}

        val afterLogin = LocalDateTime.now()

        val loginHistory = loginHistoryRepository.findByUser(userId)

        assertEquals(1, loginHistory.size)

        val login = loginHistory[0]

        assertTrue(!login.success, "The login should have failed")
        assertTrue(login.dateTime.isAfter(beforeLogin) || login.dateTime.isEqual(beforeLogin),"The date must be after or equal to beforeLogin")
        assertTrue(login.dateTime.isBefore(afterLogin) || login.dateTime.isEqual(afterLogin), "The date must be before or equal to afterLogin")

    }
}