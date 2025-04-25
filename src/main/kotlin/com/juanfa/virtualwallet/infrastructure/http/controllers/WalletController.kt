package com.juanfa.virtualwallet.infrastructure.http.controllers

import com.juanfa.virtualwallet.application.commands.CreateWalletCommand
import com.juanfa.virtualwallet.application.handlers.CreateWalletHandler
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import com.juanfa.virtualwallet.infrastructure.http.dto.CreateWalletRequestDTO
import com.juanfa.virtualwallet.infrastructure.http.dto.WalletResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/wallets")
class WalletController(
    private val createWalletHandler: CreateWalletHandler,
    private val walletRepository: WalletRepository
) {

    @PostMapping
    fun createWallet(@RequestHeader("User-ID") userId: UUID, @RequestBody requestDTO: CreateWalletRequestDTO): ResponseEntity<Any> {
        return try {
            val now = LocalDateTime.now()
            val command = CreateWalletCommand(
                id = UUID.randomUUID(),
                name = requestDTO.name,
                type = requestDTO.type,
                owner = userId,
                initialAmount = requestDTO.initialAmount,
                    create = now,
                    update = now
            )

            val wallet = createWalletHandler.handle(command)

            val responseDTO = WalletResponseDTO(
                id = wallet.id,
                name = wallet.name,
                amount = wallet.amount,
                type = wallet.type.name,
                owner = wallet.owner,
                created = wallet.created,
                updated = wallet.update
            )

            ResponseEntity(responseDTO, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            val errorResponse = mapOf("error" to (e.message ?: "Invalid input"))
            ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            val errorResponse = mapOf("error" to "An unexpected error occurred")
            ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun getWallet(@PathVariable id: UUID): ResponseEntity<Any> {
        return try {
            val wallet = walletRepository.findById(id)
                ?: return ResponseEntity(mapOf("error" to "Wallet not found"), HttpStatus.NOT_FOUND)

            val responseDTO = WalletResponseDTO(
                id = wallet.id,
                name = wallet.name,
                amount = wallet.amount,
                type = wallet.type.name,
                owner = wallet.owner,
                created = wallet.created,
                updated = wallet.update
            )

            ResponseEntity(responseDTO, HttpStatus.OK)
        } catch (e: Exception) {
            val errorResponse = mapOf("error" to "An unexpected error occurred")
            ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/user/{userId}")
    fun getUserWallets(@PathVariable userId: UUID): ResponseEntity<Any> {
        return try {
            val wallets = walletRepository.findByOwner(userId)

            val responseDTOs = wallets.map { wallet ->
                WalletResponseDTO(
                    id = wallet.id,
                    name = wallet.name,
                    amount = wallet.amount,
                    type = wallet.type.name,
                    owner = wallet.owner,
                    created = wallet.created,
                    updated = wallet.update
                )
            }

            ResponseEntity(responseDTOs, HttpStatus.OK)
        } catch (e: Exception) {
            val errorResponse = mapOf("error" to "An unexpected error occurred")
            ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}