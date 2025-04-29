package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.RejectMoneyRequestCommand
import com.juanfa.virtualwallet.old.domain.model.*
import com.juanfa.virtualwallet.old.domain.repository.MoneyRequestRepository
import com.juanfa.virtualwallet.old.domain.repository.MovementHistoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class RejectMoneyRequestHandler(
    private val moneyRequestRepository: MoneyRequestRepository,
    private val movementHistoryRepository: MovementHistoryRepository
) {
    fun handle(command: RejectMoneyRequestCommand): MoneyRequest {
        val moneyRequest = moneyRequestRepository.findById(command.requestId).orElseThrow {
            IllegalArgumentException("Money request not found")
        }

        if (moneyRequest.status != RequestStatus.PENDING) {
            throw IllegalArgumentException("Money request is already processed")
        }

        if(moneyRequest.sender == command.rejectedBy) {
            throw IllegalArgumentException("User not authorized to reject this request")
        }

        val now = LocalDateTime.now()

        val updatedMoneyRequest = moneyRequest.copy(
            status = RequestStatus.REJECTED,
            update = now
        )

        val savedRequest = moneyRequestRepository.save(updatedMoneyRequest)

        val history = MovementHistory(
            id = UUID.randomUUID(),
            originWallet = moneyRequest.wallet,
            destinyWallet = moneyRequest.recipient,
            dateTime = now,
            success = false,
            movement = null,
            failureReason = "Money request rejected by user ${command.rejectedBy}"
        )
        movementHistoryRepository.save(history)

        return savedRequest
    }
}