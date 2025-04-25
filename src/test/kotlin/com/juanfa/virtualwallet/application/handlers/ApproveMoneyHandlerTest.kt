package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.ApproveMoneyRequestCommand
import com.juanfa.virtualwallet.domain.model.*
import com.juanfa.virtualwallet.infrastructure.db.MoneyRequestRepositoryImpl
import com.juanfa.virtualwallet.infrastructure.db.MovementHistoryRepositoryImpl
import com.juanfa.virtualwallet.infrastructure.db.MovementRepositoryImpl
import com.juanfa.virtualwallet.infrastructure.db.WalletRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
class ApproveMoneyRequestHandlerTest {
    @Autowired
    private lateinit var movementHistoryRepositoryImpl: MovementHistoryRepositoryImpl

    @Autowired
    private lateinit var moneyRequestRepositoryImpl: MoneyRequestRepositoryImpl

    @Autowired
    private lateinit var movementRepositoryImpl: MovementRepositoryImpl

    @Autowired
    private lateinit var walletRepositoryImpl: WalletRepositoryImpl

    private lateinit var handler: ApproveMoneyRequestHandler

    @BeforeEach
    fun setUp() {
        handler = ApproveMoneyRequestHandler(
            moneyRequestRepositoryImpl,
            movementRepositoryImpl,
            movementHistoryRepositoryImpl,
            walletRepositoryImpl
        )
    }

    @Test
    fun testShouldApproveMoneyRequestAndCreateMovement() {
        val requestId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val senderId = UUID.randomUUID()

        val originWallet = Wallet(
            id = walletId,
            name = "Origin Wallet",
            amount = 500,
            type = WalletType.PERSONAL,
            owner = senderId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val destinyWallet = Wallet(
            id = recipientId,
            name = "Destiny Wallet",
            amount = 100,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        walletRepositoryImpl.save(originWallet)
        walletRepositoryImpl.save(destinyWallet)


        val moneyRequest = MoneyRequest(
            id = requestId,
            sender = senderId,
            recipient = recipientId,
            amount = 100,
            status = RequestStatus.PENDING,
            wallet = walletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        moneyRequestRepositoryImpl.save(moneyRequest)

        val approvedBy = UUID.randomUUID()
        val command = ApproveMoneyRequestCommand(
            requestId = moneyRequest.id,
            approvedBy = approvedBy
        )

        val result = handler.handle(command)

        val updatedMoneyRequest = moneyRequestRepositoryImpl.findById(moneyRequest.id).get()
        assertEquals(RequestStatus.APPROVED, updatedMoneyRequest.status)


        assertNotNull(result)
        assertEquals(walletId, result.originWallet)
        assertEquals(recipientId, result.destinyWallet)
        assertEquals(moneyRequest.amount, result.amount)
        assertEquals(MovementStatus.PENDING, result.status)
        assertEquals(approvedBy, result.startMovementBy)

        val updatedOriginWallet = walletRepositoryImpl.findById(walletId)
            ?: throw IllegalStateException("Origin wallet not found")
        val updatedDestinyWallet = walletRepositoryImpl.findById(recipientId)
            ?: throw IllegalStateException("Destiny wallet not found")

        assertEquals(400, updatedOriginWallet.amount)
        assertEquals(200, updatedDestinyWallet.amount)
    }

    @Test
    fun testShouldThrowExceptionWhenMoneyRequestNotFound() {
        val command = ApproveMoneyRequestCommand(
            requestId = UUID.randomUUID(),
            approvedBy = UUID.randomUUID()
        )
        val exception = assertFailsWith<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Money request not found", exception.message)

    }

    @Test
    fun testShouldThrowWhenMoneyRequestIsAlreadyProcessed() {
        val requestId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()

        val moneyRequest = MoneyRequest(
            id = requestId,
            sender = senderId,
            recipient = recipientId,
            amount = 350,
            status = RequestStatus.APPROVED,
            wallet = walletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        moneyRequestRepositoryImpl.save(moneyRequest)

        val command = ApproveMoneyRequestCommand(
            requestId = requestId,
            approvedBy = UUID.randomUUID()
        )
        val exception = assertFailsWith<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Money request is already processed", exception.message)
    }

    @Test
    fun testShouldCreateMovementHistoryWhenMoneyRequestIsApproved() {
        val requestId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val senderId = UUID.randomUUID()

        val originWallet = Wallet(
            id = walletId,
            name = "Origin Wallet",
            amount = 500,
            type = WalletType.PERSONAL,
            owner = senderId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val destinyWallet = Wallet(
            id = recipientId,
            name = "Destiny Wallet",
            amount = 100,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        walletRepositoryImpl.save(originWallet)
        walletRepositoryImpl.save(destinyWallet)

        val moneyRequest = MoneyRequest(
            id = requestId,
            sender = senderId,
            recipient = recipientId,
            amount = 122,
            status = RequestStatus.PENDING,
            wallet = walletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        moneyRequestRepositoryImpl.save(moneyRequest)

        val approvedBy = UUID.randomUUID()
        val command = ApproveMoneyRequestCommand(
            requestId = requestId,
            approvedBy = approvedBy
        )
        val movement = handler.handle(command)

        val createHistories = movementHistoryRepositoryImpl.findByMovement(movement.id)
        assertNotNull(createHistories)
        assert(createHistories.isNotEmpty())
        val createHistory = createHistories.first()

        assertEquals(walletId, createHistory.originWallet)
        assertEquals(recipientId, createHistory.destinyWallet)
        assertEquals(true, createHistory.success)
        assertEquals(movement.id, createHistory.movement)
    }

    @Test
    fun testShouldReflectBalanceInWalletsWhenMoneyRequestIsApproved() {
        val originWalletId = UUID.randomUUID()
        val destinyWalletId = UUID.randomUUID()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val approvedBy = UUID.randomUUID()
        val amount = 200

        val originWallet = Wallet(
            id = originWalletId,
            name = "Origin Wallet",
            amount = 1000,
            type = WalletType.PERSONAL,
            owner = senderId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val destinyWallet = Wallet(
            id = destinyWalletId,
            name = "Destiny Wallet",
            amount = 300,
            type = WalletType.PERSONAL,
            owner = recipientId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        walletRepositoryImpl.save(originWallet)
        walletRepositoryImpl.save(destinyWallet)

        val moneyRequest = MoneyRequest(
            id = UUID.randomUUID(),
            sender = senderId,
            recipient = destinyWalletId,
            amount = amount,
            status = RequestStatus.PENDING,
            wallet = originWalletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        moneyRequestRepositoryImpl.save(moneyRequest)

        val command = ApproveMoneyRequestCommand(
            requestId = moneyRequest.id,
            approvedBy = approvedBy
        )

        handler.handle(command)

        val updatedOriginWallet = walletRepositoryImpl.findById(originWalletId)
            ?: throw IllegalStateException("Origin wallet not found")
        val updatedDestinyWallet = walletRepositoryImpl.findById(destinyWalletId)
            ?: throw IllegalStateException("Destiny wallet not found")

        assertEquals(800, updatedOriginWallet.amount)
        assertEquals(500, updatedDestinyWallet.amount)
    }

    @Test
    fun testShouldThrowExceptionWhenOriginWalletHasInsufficientBalance() {
        val originWalletId = UUID.randomUUID()
        val destinyWalletId = UUID.randomUUID()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val approvedBy = UUID.randomUUID()
        val amount = 2000

        val originWallet = Wallet(
            id = originWalletId,
            name = "Origin Wallet",
            amount = 1000,
            type = WalletType.PERSONAL,
            owner = senderId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val destinyWallet = Wallet(
            id = destinyWalletId,
            name = "Destiny Wallet",
            amount = 300,
            type = WalletType.PERSONAL,
            owner = recipientId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        walletRepositoryImpl.save(originWallet)
        walletRepositoryImpl.save(destinyWallet)

        val moneyRequest = MoneyRequest(
            id = UUID.randomUUID(),
            sender = senderId,
            recipient = destinyWalletId,
            amount = amount,
            status = RequestStatus.PENDING,
            wallet = originWalletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        moneyRequestRepositoryImpl.save(moneyRequest)

        val command = ApproveMoneyRequestCommand(
            requestId = moneyRequest.id,
            approvedBy = approvedBy
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Insufficient balance in origin wallet", exception.message)

        val unmodifiedOriginWallet = walletRepositoryImpl.findById(originWalletId)
            ?: throw IllegalStateException("Origin wallet not found")
        val unmodifiedDestinyWallet = walletRepositoryImpl.findById(destinyWalletId)
            ?: throw IllegalStateException("Destiny wallet not found")

        assertEquals(1000, unmodifiedOriginWallet.amount)
        assertEquals(300, unmodifiedDestinyWallet.amount)
    }
    @Test
    fun testShouldCorrectlyUpdateBalancesOfBothWalletsAfterApproval() {
        val requestId = UUID.randomUUID()
        val originWalletId = UUID.randomUUID()
        val destinyWalletId = UUID.randomUUID()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val amount = 200

        val originWallet = Wallet(
            id = originWalletId,
            name = "sender wallet",
            amount = 1000,
            type = WalletType.PERSONAL,
            owner = senderId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val destinyWallet = Wallet(
            id = destinyWalletId,
            name = "recipient wallet",
            amount = 500,
            type = WalletType.PERSONAL,
            owner = recipientId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        val moneyRequest = MoneyRequest(
            id = requestId,
            sender = senderId,
            recipient = destinyWalletId,
            amount = amount,
            status = RequestStatus.PENDING,
            wallet = originWalletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepositoryImpl.save(originWallet)
        walletRepositoryImpl.save(destinyWallet)
        moneyRequestRepositoryImpl.save(moneyRequest)

        val command = ApproveMoneyRequestCommand(
            requestId = requestId,
            approvedBy = UUID.randomUUID()
        )
        handler.handle(command)

        val updatedOriginWallet = walletRepositoryImpl.findById(originWalletId)
            ?: throw IllegalStateException("Origin wallet not found")
        val updatedDestinyWallet = walletRepositoryImpl.findById(destinyWalletId)
            ?: throw IllegalStateException("Destiny wallet not found")

        assertEquals(800, updatedOriginWallet.amount)
        assertEquals(700, updatedDestinyWallet.amount)
    }
    @Test
    fun testShouldThrowExceptionIfApproverIsSameAsRequester() {
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val amount = 100

        val originWallet = Wallet(
            id = walletId,
            name = "Origin wallet",
            amount = 500,
            type = WalletType.COMPANY,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        walletRepositoryImpl.save(originWallet)

        val moneyRequest = MoneyRequest(
            id = UUID.randomUUID(),
            sender = userId,
            recipient = UUID.randomUUID(),
            amount = amount,
            status = RequestStatus.PENDING,
            wallet = walletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        moneyRequestRepositoryImpl.save(moneyRequest)

        val command = ApproveMoneyRequestCommand(
            requestId = moneyRequest.id,
            approvedBy = userId
        )
        val exception = assertFailsWith<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("User not authorized to approve this request", exception.message)

    }
}