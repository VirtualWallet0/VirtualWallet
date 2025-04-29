package com.juanfa.virtualwallet.old.infrastructure.http.controllers

import com.juanfa.virtualwallet.old.domain.model.Wallet
import com.juanfa.virtualwallet.old.domain.model.WalletType
import com.juanfa.virtualwallet.old.domain.repository.WalletRepository
import com.juanfa.virtualwallet.old.infrastructure.http.dto.CreateWalletRequestDTO
import com.juanfa.virtualwallet.old.infrastructure.http.dto.WalletResponseDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.*

class WalletControllerTest {

    private lateinit var createWalletHandler: com.juanfa.virtualwallet.old.application.handlers.CreateWalletHandler
    private lateinit var walletRepository: WalletRepository
    private lateinit var walletController: WalletController

    inline fun <reified T> anyNonNull(): T = Mockito.any(T::class.java)

    @BeforeEach
    fun setup() {
        createWalletHandler = mock(com.juanfa.virtualwallet.old.application.handlers.CreateWalletHandler::class.java)
        walletRepository = mock(WalletRepository::class.java)
        walletController = WalletController(createWalletHandler, walletRepository)
    }

    @Test
    fun `createWallet should return created when input is valid`() {
        val userId = UUID.randomUUID()
        val requestDTO = CreateWalletRequestDTO(
            name = "Test Wallet",
            type = "PERSONAL",
            initialAmount = 100
        )

        val now = LocalDateTime.now()
        val wallet = Wallet(
            id = UUID.randomUUID(),
            name = "Test Wallet",
            amount = 100,
            type = WalletType.PERSONAL,
            owner = userId,
            created = now,
            update = now
        )

        `when`(createWalletHandler.handle(anyNonNull())).thenReturn(wallet)

        val response = walletController.createWallet(userId, requestDTO)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        val body = response.body as WalletResponseDTO
        assertEquals(wallet.id, body.id)
        assertEquals("Test Wallet", body.name)
        assertEquals(100, body.amount)
        assertEquals("PERSONAL", body.type)
    }

    @Test
    fun `createWallet should return bad request when handler throws IllegalArgumentException`() {
        val userId = UUID.randomUUID()
        val requestDTO = CreateWalletRequestDTO(
            name = "",
            type = "PERSONAL",
            initialAmount = 100
        )

        `when`(createWalletHandler.handle(anyNonNull()))
            .thenThrow(IllegalArgumentException("Wallet name cannot be blank"))

        val response = walletController.createWallet(userId, requestDTO)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val body = response.body as Map<*, *>
        assertEquals("Wallet name cannot be blank", body["error"])
    }

    @Test
    fun `createWallet should return internal server error when unexpected exception occurs`() {
        val userId = UUID.randomUUID()
        val requestDTO = CreateWalletRequestDTO(
            name = "Test Wallet",
            type = "PERSONAL",
            initialAmount = 100
        )

        `when`(createWalletHandler.handle(anyNonNull()))
            .thenThrow(RuntimeException("Unexpected error"))

        val response = walletController.createWallet(userId, requestDTO)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        val body = response.body as Map<*, *>
        assertEquals("An unexpected error occurred", body["error"])
    }

    @Test
    fun `getWallet should return wallet when it exists`() {
        val walletId = UUID.randomUUID()
        val wallet = Wallet(
            id = walletId,
            name = "My Wallet",
            amount = 500,
            type = WalletType.PERSONAL,
            owner = UUID.randomUUID(),
            created = LocalDateTime.now(),
            update = LocalDateTime.now()
        )

        `when`(walletRepository.findById(walletId)).thenReturn(wallet)

        val response = walletController.getWallet(walletId)

        assertEquals(HttpStatus.OK, response.statusCode)
        val body = response.body as WalletResponseDTO
        assertEquals(walletId, body.id)
        assertEquals("My Wallet", body.name)
        assertEquals(500, body.amount)
    }

    @Test
    fun `getWallet should return not found when wallet does not exist`() {
        val walletId = UUID.randomUUID()

        `when`(walletRepository.findById(walletId)).thenReturn(null)

        val response = walletController.getWallet(walletId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val body = response.body as Map<*, *>
        assertEquals("Wallet not found", body["error"])
    }

    @Test
    fun `getUserWallets should return list of user wallets`() {
        val userId = UUID.randomUUID()
        val now = LocalDateTime.now()

        val wallet1 = Wallet(
            id = UUID.randomUUID(),
            name = "Personal Wallet",
            amount = 200,
            type = WalletType.PERSONAL,
            owner = userId,
            created = now,
            update = now
        )

        val wallet2 = Wallet(
            id = UUID.randomUUID(),
            name = "Company Wallet",
            amount = 1000,
            type = WalletType.COMPANY,
            owner = userId,
            created = now,
            update = now
        )

        val wallets = listOf(wallet1, wallet2)

        `when`(walletRepository.findByOwner(userId)).thenReturn(wallets)

        val response = walletController.getUserWallets(userId)

        assertEquals(HttpStatus.OK, response.statusCode)
        val body = response.body as List<*>
        assertEquals(2, body.size)

        val firstWallet = body[0] as WalletResponseDTO
        assertEquals(wallet1.id, firstWallet.id)
        assertEquals("Personal Wallet", firstWallet.name)

        val secondWallet = body[1] as WalletResponseDTO
        assertEquals(wallet2.id, secondWallet.id)
        assertEquals("Company Wallet", secondWallet.name)
    }

    @Test
    fun `getUserWallets should return empty list when user has no wallets`() {
        val userId = UUID.randomUUID()

        `when`(walletRepository.findByOwner(userId)).thenReturn(emptyList())

        val response = walletController.getUserWallets(userId)

        assertEquals(HttpStatus.OK, response.statusCode)
        val body = response.body as List<*>
        assertTrue(body.isEmpty())
    }
}