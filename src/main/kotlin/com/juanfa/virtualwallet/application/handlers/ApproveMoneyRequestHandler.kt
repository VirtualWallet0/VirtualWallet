package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.ApproveMoneyRequestCommand
import com.juanfa.virtualwallet.domain.model.*
import com.juanfa.virtualwallet.domain.repository.MoneyRequestRepository
import com.juanfa.virtualwallet.domain.repository.MovementHistoryRepository
import com.juanfa.virtualwallet.domain.repository.MovementRepository
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ApproveMoneyRequestHandler(
    private val moneyRequestRepository: MoneyRequestRepository,
    private val movementRepository: MovementRepository,
    private val movementHistoryRepository: MovementHistoryRepository,
    private val walletRepository: WalletRepository
) {
    fun handle(command: ApproveMoneyRequestCommand): Movement {
        val moneyRequest = moneyRequestRepository.findById(command.requestId).orElseThrow {
            IllegalArgumentException("Money request not found")
        }

        if (moneyRequest.status != RequestStatus.PENDING) {
            throw IllegalArgumentException("Money request is already processed")
        }
        if(moneyRequest.sender == command.approvedBy) {
            throw IllegalArgumentException("User not authorized to approve this request")
        }

        val originWallet = walletRepository.findById(moneyRequest.wallet)
            ?: throw IllegalArgumentException("Origin wallet not found")

        val destinyWallet = walletRepository.findById(moneyRequest.recipient)
            ?: throw IllegalArgumentException("Destiny wallet not found")

        if(originWallet.amount < moneyRequest.amount){
            throw IllegalArgumentException("Insufficient balance in origin wallet")
        }

        walletRepository.save(originWallet.copy(
            amount = originWallet.amount - moneyRequest.amount,
            update = LocalDateTime.now()
        ))

        walletRepository.save(destinyWallet.copy(
            amount = destinyWallet.amount + moneyRequest.amount,
            update = LocalDateTime.now()
        ))

        val now = LocalDateTime.now()

        val updatedMoneyRequest = moneyRequest.copy(
            status = RequestStatus.APPROVED,
            update = now
        )
        moneyRequestRepository.save(updatedMoneyRequest)

        val movement = Movement(
            id = UUID.randomUUID(),
            originWallet = moneyRequest.wallet,
            destinyWallet = moneyRequest.recipient,
            amount = moneyRequest.amount,
            status = MovementStatus.PENDING,
            startMovementTime = now,
            startMovementBy = command.approvedBy,
            approveMovementTime = null,
            approveMovementBy = null,
            created = now,
            update = now
        )

        movementRepository.save(movement)

        val history = MovementHistory(
            id = UUID.randomUUID(),
            originWallet = movement.originWallet,
            destinyWallet = movement.destinyWallet,
            dateTime = now,
            success = true,
            movement = movement.id,
            failureReason = null
        )
        movementHistoryRepository.save(history)

        return movement
    }

}