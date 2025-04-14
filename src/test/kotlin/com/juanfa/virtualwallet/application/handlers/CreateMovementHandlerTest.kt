package com.juanfa.virtualwallet.application.handlers


import com.juanfa.virtualwallet.application.commands.CreateMovementCommand
import com.juanfa.virtualwallet.domain.model.MovementStatus
import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.model.WalletType
import com.juanfa.virtualwallet.domain.repository.MovementRepository
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class CreateMovementHandlerTest {

    @Autowired
    private lateinit var createMovementHandler: CreateMovementHandler

    @Autowired
    private lateinit var movementRepository: MovementRepository

    @Autowired
    private lateinit var walletRepository: WalletRepository

    @Autowired
    private lateinit var handler: CreateMovementHandler

    @Test
    fun testShouldCreateMovementSucces() {
        val id = UUID.randomUUID()
        val originWallet = UUID.randomUUID()
        val destinyWallet = UUID.randomUUID()
        val userWhoStarted = UUID.randomUUID()
        val amount = 100
        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val status = MovementStatus.PENDING

        val command = CreateMovementCommand(
            id = id,
            originWallet = originWallet,
            destinyWallet = destinyWallet,
            amount = amount,
            status = status,
            startMovementTime = now,
            startMovementBy = userWhoStarted,
            approveMovementTime = null,
            approveMovementBy = null,
            created = now,
            update = now
        )
        createMovementHandler.handle(command)

        val saveMovement = movementRepository.findById(id).orElse(null)

        assertNotNull(saveMovement)
        assertEquals(originWallet, saveMovement.originWallet)
        assertEquals(destinyWallet, saveMovement.destinyWallet)
        assertEquals(amount, saveMovement.amount)
        assertEquals(status, saveMovement.status)
        assertEquals(now, saveMovement.startMovementTime.truncatedTo(ChronoUnit.SECONDS))
        assertEquals(userWhoStarted, saveMovement.startMovementBy)
    }

    @Test
    fun testShouldThrowExceptionWhenAmountIsNegative() {
        val command = CreateMovementCommand(
            id = UUID.randomUUID(),
            originWallet = UUID.randomUUID(),
            destinyWallet = UUID.randomUUID(),
            amount = -1000,
            status = MovementStatus.PENDING,
            startMovementTime = LocalDateTime.now(),
            startMovementBy = UUID.randomUUID(),
            approveMovementBy = null,
            approveMovementTime = null,
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )
        val exception = assertThrows(IllegalArgumentException::class.java) {
            createMovementHandler.handle(command)
        }
        assertEquals("Amount must be positive", exception.message)
    }

        @Test
        fun testShouldCreateAMovementSuccesfullyWithNullApproveFields() {
            val originWallet = walletRepository.save(createTestWallet("Origin Wallet"))
            val destinyWallet = walletRepository.save(createTestWallet("Destiny Wallet"))

            assertNotNull(originWallet.id)
            assertNotNull(destinyWallet.id)

            val command = CreateMovementCommand(
                id = UUID.randomUUID(),
                originWallet = originWallet.id,
                destinyWallet = destinyWallet.id,
                amount = 350,
                status = MovementStatus.PENDING,
                startMovementTime = LocalDateTime.now(),
                startMovementBy = UUID.randomUUID(),
                approveMovementTime = null,
                approveMovementBy = null,
                created = LocalDateTime.now(),
                update = LocalDateTime.now()
            )
            val result = handler.handle(command)
            assertNotNull(result)
            val saved = movementRepository.findById(command.id).get()

            assertEquals(command.id, saved.id)
            assertEquals(command.originWallet, saved.originWallet)
            assertEquals(command.destinyWallet, saved.destinyWallet)
            assertEquals(command.amount, saved.amount)
            assertEquals(command.status, saved.status)
            assertEquals(command.startMovementTime.withNano(0), saved.startMovementTime.withNano(0))
            assertEquals(command.startMovementBy, saved.startMovementBy)
            assertEquals(null, saved.approveMovementTime)
            assertEquals(null, saved.approveMovementBy)
        }

        private fun createTestWallet(name: String): Wallet {
            return Wallet(
                id = UUID.randomUUID(),
                name = name,
                amount = 1000,
                type = WalletType.PERSONAL,
                owner = UUID.randomUUID(),
                created = LocalDateTime.now(),
                update = LocalDateTime.now()
            )
        }
}