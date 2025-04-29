package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.CreateWalletCommand
import com.juanfa.virtualwallet.old.domain.model.WalletType
import com.juanfa.virtualwallet.old.infrastructure.db.WalletRepositoryJPA
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNotNull

@SpringBootTest
class CreateWalletHandlerTest {

    @Autowired
    lateinit var handler: com.juanfa.virtualwallet.old.application.handlers.CreateWalletHandler

    @Autowired
    lateinit var walletRepositoryJPA: WalletRepositoryJPA

    @Autowired
    lateinit var createWalletHandler: com.juanfa.virtualwallet.old.application.handlers.CreateWalletHandler

    @Test
    fun testTheWalletShouldBeCreatedAndSave() {
        val command = CreateWalletCommand(
            id = UUID.randomUUID(),
            name = "Wallet432",
            initialAmount = 4355654,
            type = "PersoNal",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        handler.handle(command)

        val wallet = walletRepositoryJPA.findById(command.id).orElse(null)

        assertNotNull(wallet)
        assertEquals(command.name, wallet.name)
        assertEquals(command.initialAmount, wallet.amount)
    }

    @Test
    fun testShouldThrowExceptionWhenWalletTypeIsInvalid() {
        val command = CreateWalletCommand(
            id = UUID.randomUUID(),
            name = "Invalid Wallet",
            initialAmount = 100,
            type = "INVALID TYPE",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        assertThrows(IllegalArgumentException::class.java)
        {
            handler.handle(command)
        }
    }

    @Test
    fun testShouldCreateWalletWhitValidData() {
        val command = CreateWalletCommand(
            id = UUID.randomUUID(),
            name = "Personal Wallet",
            initialAmount = 2500,
            type = "PERSONAL",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        val wallet = handler.handle(command)

        assertEquals(command.id, wallet.id)
        assertEquals(command.name, wallet.name)
        assertEquals(command.initialAmount, wallet.amount)
        assertEquals(command.owner, wallet.owner)
        assertEquals(command.create, wallet.created)
        assertEquals(command.update, wallet.update)
        assertEquals(WalletType.PERSONAL, wallet.type)

    }

    @Test
    fun testShouldThrowExceptionWhenAmountIsNegative() {
        val command = CreateWalletCommand(
            id = UUID.randomUUID(),
            name = "Negative Wallet",
            initialAmount = -500,
            type = "PERSONAL",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        val exception = assertThrows<IllegalArgumentException> {
            createWalletHandler.handle(command)
        }
        assertEquals("Amount cannot be negative", exception.message)

    }

    @Test
    fun testThrowExceptionWhenNameIsBlanck() {
        val command = CreateWalletCommand(
            id = UUID.randomUUID(),
            name = "",
            initialAmount = 888,
            type = "PERSONAL",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        val exception = assertThrows<IllegalArgumentException> {
            createWalletHandler.handle(command)
        }
        assertEquals("Wallet name cannot be blank", exception.message)
    }

    @Test
    fun testShouldThrowExceptionWhenWalletIdAlreadyExists() {
        val existingId = UUID.randomUUID()

        val firstCommand = CreateWalletCommand(
            id = existingId,
            name = "wallet",
            initialAmount = 250,
            type = "COMPANY",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        createWalletHandler.handle(firstCommand)
        
        val secondCommand = CreateWalletCommand(
            id = existingId,
            name = "walletDuplicate",
            initialAmount = 888,
            type = "COMPANY",
            owner = UUID.randomUUID(),
            create = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val exception = assertThrows<IllegalArgumentException> {
            createWalletHandler.handle(secondCommand)
        }
        assertEquals("Wallet whit ID $existingId already exists", exception.message)
    }
}