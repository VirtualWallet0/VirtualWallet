package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.CreateRoleCommand
import com.juanfa.virtualwallet.old.domain.model.RoleType
import com.juanfa.virtualwallet.old.domain.model.User
import com.juanfa.virtualwallet.old.domain.model.Wallet
import com.juanfa.virtualwallet.old.domain.model.WalletType
import com.juanfa.virtualwallet.old.domain.repository.RoleRepository
import com.juanfa.virtualwallet.old.domain.repository.UserRepository
import com.juanfa.virtualwallet.old.domain.repository.WalletRepository
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
            created = assign,
            update = assign,
            password = "123456"
        )
        userRepository.save(user)

        val wallet = Wallet(
            id = walletId,
            name = "Main Wallet",
            amount = 1000,
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
    @Test
    fun testShouldFailWhenAssigningRoleToNonCompanyWallet() {
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val assign = LocalDateTime.now()

        val user = User(
            id = userId,
            name = "user1",
            created = assign,
            update = assign,
            password = "pswd"
        )
        userRepository.save(user)

        val wallet = Wallet(
            id = walletId,
            name = "personalwallet",
            amount = 888,
            type = WalletType.PERSONAL,
            owner = userId,
            created = assign,
            update = assign
        )
        walletRepository.save(wallet)

        val command = CreateRoleCommand(
            id = UUID.randomUUID(),
            user = userId,
            wallet = walletId,
            roleType = RoleType.ADMIN,
            assign = assign
        )

        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Roles can only be assigned to wallets of type COMPANY", exception.message)
    }
    @Test
    fun testShouldFailWhenAssignDateIsInTheFuture() {
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val futureDate = LocalDateTime.now().plusDays(1)

        val user = User(
            id = userId,
            name = "UserFuture",
            created = LocalDateTime.now(),
            update = LocalDateTime.now(),
            password = "555"
        )
        userRepository.save(user)

        val wallet = Wallet(
            id = walletId,
            name = "walletfuture",
            amount = 1234,
            type = WalletType.COMPANY,
            owner = userId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepository.save(wallet)

        val command = CreateRoleCommand(
            id = UUID.randomUUID(),
            user = userId,
            wallet = walletId,
            roleType = RoleType.ADMIN,
            assign = futureDate
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Assign date cannot be in the future", exception.message)
    }
    @Test
    fun testShouldAllowReassigningRoleAfterDeletion() {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()
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
            name = "Company Wallet",
            amount = 5000,
            type = WalletType.COMPANY,
            owner = userId,
            created = assign,
            update = assign
        )
        walletRepository.save(wallet)

        val firstCommand = CreateRoleCommand(
            id = id1,
            user = userId,
            wallet = walletId,
            roleType = RoleType.OPERATOR,
            assign = assign
        )
        handler.handle(firstCommand)

        val existingRole = roleRepository.findById(id1)
        assertTrue(existingRole.isPresent)

        roleRepository.delete(id1)


        val secondCommand = CreateRoleCommand(
            id = id2,
            user = userId,
            wallet = walletId,
            roleType = RoleType.OPERATOR,
            assign = assign
        )

        handler.handle(secondCommand)

        val saved = roleRepository.findById(id2)
        assertTrue(saved.isPresent)
        assertEquals(userId, saved.get().user)
        assertEquals(walletId, saved.get().wallet)
        assertEquals(RoleType.OPERATOR, saved.get().roleType)
    }


}