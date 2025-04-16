package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateRoleCommand
import com.juanfa.virtualwallet.domain.model.RoleType
import com.juanfa.virtualwallet.domain.model.User
import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.model.WalletType
import com.juanfa.virtualwallet.domain.repository.RoleRepository
import com.juanfa.virtualwallet.domain.repository.UserRepository
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class CreateRoleHandlerTest {

    @Autowired
    private lateinit var handler: CreateRoleHandler

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var userRepository: UserRepository


    @Test
    fun testShouldCreateRoleSuccessfully() {
        val id = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val roleType = RoleType.ADMIN
        val assign = LocalDateTime.now()

        val user = User(
            id = userId,
            name = "Juan",
            created = LocalDateTime.now(),
            update = LocalDateTime.now(),
            password = "123456"
        )
        userRepository.save(user)

        val wallet = Wallet(
            id = walletId,
            name = "Main Wallet",
            amount = 1000,
            type = WalletType.PERSONAL,
            owner = userId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepository.save(wallet)

        val command = CreateRoleCommand(
            id = id,
            user = userId,
            wallet = walletId,
            roleType = roleType,
            assign = assign
        )
        handler.handle(command)

        val save = roleRepository.findById(id)

        assertTrue(save.isPresent)
        assertEquals(command.user, save.get().user)
        assertEquals(command.wallet, save.get().wallet)
        assertEquals(command.roleType, save.get().roleType)
        assertEquals(command.assign.truncatedTo(ChronoUnit.SECONDS), save.get().assign.truncatedTo(ChronoUnit.SECONDS))
    }
    @Test
    fun testShouldFailWhenRoleTypeIsInvalid() {
        val id = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val assign = LocalDateTime.now()

        val exception = assertThrows<IllegalArgumentException> {
            RoleType.valueOf("MANAGER")
        }

        assertEquals("No enum constant com.juanfa.virtualwallet.domain.model.RoleType.MANAGER", exception.message)
    }
    @Test
    fun testShouldFailWhenUserOrWalletIsNull() {
        val exception = assertThrows<IllegalArgumentException> {
            val command = CreateRoleCommand(
                id = UUID.randomUUID(),
                user = UUID.fromString("00000000-0000-0000-0000-000000000000"),
                wallet = UUID.fromString("00000000-0000-0000-0000-000000000000"),
                roleType = RoleType.ADMIN,
                assign = LocalDateTime.now()
            )
            handler.handle(command)
        }
        assertEquals("User or Wallet ID is invalid", exception.message)
    }
    @Test
    fun testShouldFailWhenUserDoesNotExist() {
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()

        val wallet = Wallet(
            id = walletId,
            name = "Wallet",
            amount = 1234,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepository.save(wallet)

        val command = CreateRoleCommand(
            id = UUID.randomUUID(),
            user = userId,
            wallet = walletId,
            roleType = RoleType.ADMIN,
            assign = LocalDateTime.now()
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("The user does not exist", exception.message)


    }
    @Test
    fun testShouldFailWhenWalletDoesNotExist() {
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            name = "pepe",
            created = LocalDateTime.now(),
            update = LocalDateTime.now(),
            password = "11111"
        )
        userRepository.save(user)

        val walletId = UUID.randomUUID()

        val command = CreateRoleCommand(
            id = UUID.randomUUID(),
            user = userId,
            wallet = walletId,
            roleType = RoleType.ADMIN,
            assign = LocalDateTime.now()
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Wallet does not exist", exception.message)
    }
    @Test
    fun testShouldFailWhenRoleAlreadyExist() {
        val id = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val assign = LocalDateTime.now()

        val user = User(
            id = userId,
            name = "lele",
            created = assign,
            update = assign,
            password = "2323"
        )
        userRepository.save(user)

        val wallet = Wallet(
            id = walletId,
            name = "myWaallet",
            amount = 3233,
            type = WalletType.COMPANY,
            owner = userId,
            created = assign,
            update = assign
        )
        walletRepository.save(wallet)

        val command = CreateRoleCommand(
            id = id,
            user = userId,
            wallet = walletId,
            roleType = RoleType.ADMIN,
            assign = assign
        )
        handler.handle(command)

        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command.copy(id = UUID.randomUUID()))
        }
        assertEquals("Role already exists for this user and wallet", exception.message)
    }


}