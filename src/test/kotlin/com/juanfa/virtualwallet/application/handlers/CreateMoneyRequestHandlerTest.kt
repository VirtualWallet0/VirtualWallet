package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateMoneyRequestCommand
import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.model.WalletType
import com.juanfa.virtualwallet.domain.repository.MoneyRequestRepository
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class CreateMoneyRequestHandlerTest {
    @Autowired
    private lateinit var handler: CreateMoneyRequestHandler

    @Autowired
    private lateinit var walletRepository: WalletRepository

    @Autowired
    private lateinit var moneyRequestRepository: MoneyRequestRepository

    @Test
    fun testShouldCreateMoneyRequestSuccessfully() {

        val wallet = Wallet(
            id = UUID.randomUUID(),
            name = "pepito",
            amount = 1000,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepository.save(wallet)

        val sender = UUID.randomUUID()
        val recipient = UUID.randomUUID()
        val requestId = UUID.randomUUID()

        val command = CreateMoneyRequestCommand(
            id = requestId,
            sender = sender,
            recipient = recipient,
            amount = 333,
            wallet = wallet.id
        )
        handler.handle(command)

        val save = moneyRequestRepository.findById(command.id).orElseThrow(){IllegalArgumentException("Money request not found")}

        assertEquals(command.sender, save.sender)
        assertEquals(command.recipient, save.recipient)
        assertEquals(command.amount, save.amount)
    }
    @Test
    fun testShouldThrowExceptionWhenWalletDoesNotExist() {
        val nonExistentWalletId = UUID.randomUUID()
        val command = CreateMoneyRequestCommand(
            id = UUID.randomUUID(),
            sender = UUID.randomUUID(),
            recipient = UUID.randomUUID(),
            amount = 230,
            wallet = nonExistentWalletId
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Wallet not found", exception.message)
    }
    @Test
    fun testShouldNotCreateMoneyRequestWithInvalidAmount() {
        val wallet = Wallet(
            id = UUID.randomUUID(),
            name = "pepe",
            amount = 100,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepository.save(wallet)

        val command = CreateMoneyRequestCommand(
            id = UUID.randomUUID(),
            sender = UUID.randomUUID(),
            recipient = UUID.randomUUID(),
            amount = 0,
            wallet = wallet.id
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Amount must be greater than 0", exception.message)
    }
    @Test
    fun testShouldThrowExceptionIfSenderAndRecipientAreTheSame() {
        val wallet = Wallet(
            id = UUID.randomUUID(),
            name = "polo",
            amount = 1000,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepository.save(wallet)

        val sender = UUID.randomUUID()
        val recipient = sender
        val requestId = UUID.randomUUID()

        val command = CreateMoneyRequestCommand(
            id = requestId,
            sender = sender,
            recipient = recipient,
            amount = 255,
            wallet = wallet.id
        )
        val exception = assertThrows<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Sender and recipient cannot be the same", exception.message)
    }

}