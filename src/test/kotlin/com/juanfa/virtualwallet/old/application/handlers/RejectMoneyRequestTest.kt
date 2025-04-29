package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.RejectMoneyRequestCommand
import com.juanfa.virtualwallet.old.domain.model.*
import com.juanfa.virtualwallet.old.infrastructure.db.MoneyRequestRepositoryImpl
import com.juanfa.virtualwallet.old.infrastructure.db.MovementHistoryRepositoryImpl
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
class RejectMoneyRequestHandlerTest {
    @Autowired
    private lateinit var movementHistoryRepositoryImpl: MovementHistoryRepositoryImpl

    @Autowired
    private lateinit var moneyRequestRepositoryImpl: MoneyRequestRepositoryImpl

    private lateinit var handler: RejectMoneyRequestHandler

    @BeforeEach
    fun setUp() {
        handler = RejectMoneyRequestHandler(
            moneyRequestRepositoryImpl,
            movementHistoryRepositoryImpl
        )
    }

    @Test
    fun testShouldRejectMoneyRequest() {
        val requestId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val senderId = UUID.randomUUID()

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

        val rejectedBy = UUID.randomUUID()
        val command = RejectMoneyRequestCommand(
            requestId = moneyRequest.id,
            rejectedBy = rejectedBy
        )

        val result = handler.handle(command)

        val updatedMoneyRequest = moneyRequestRepositoryImpl.findById(moneyRequest.id).get()
        assertEquals(RequestStatus.REJECTED, updatedMoneyRequest.status)

        assertNotNull(result)
        assertEquals(RequestStatus.REJECTED, result.status)

        val rejectHistories = movementHistoryRepositoryImpl.findByOriginWalletAndDestinyWallet(
            walletId, recipientId
        )
        assert(rejectHistories.isNotEmpty())

        val rejectHistory = rejectHistories.find {
            it.success == false && it.movement == null
        }
        assertNotNull(rejectHistory)
        assertEquals(walletId, rejectHistory.originWallet)
        assertEquals(recipientId, rejectHistory.destinyWallet)
        assertNotNull(rejectHistory.failureReason)
    }

    @Test
    fun testShouldThrowExceptionWhenMoneyRequestNotFound() {
        val command = RejectMoneyRequestCommand(
            requestId = UUID.randomUUID(),
            rejectedBy = UUID.randomUUID()
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

        val command = RejectMoneyRequestCommand(
            requestId = requestId,
            rejectedBy = UUID.randomUUID()
        )
        val exception = assertFailsWith<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("Money request is already processed", exception.message)
    }

    @Test
    fun testShouldThrowExceptionIfRejectorIsSameAsRequester() {
        val userId = UUID.randomUUID()
        val walletId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        val amount = 100

        val moneyRequest = MoneyRequest(
            id = UUID.randomUUID(),
            sender = userId,
            recipient = recipientId,
            amount = amount,
            status = RequestStatus.PENDING,
            wallet = walletId,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        moneyRequestRepositoryImpl.save(moneyRequest)

        val command = RejectMoneyRequestCommand(
            requestId = moneyRequest.id,
            rejectedBy = userId
        )
        val exception = assertFailsWith<IllegalArgumentException> {
            handler.handle(command)
        }
        assertEquals("User not authorized to reject this request", exception.message)
    }
}