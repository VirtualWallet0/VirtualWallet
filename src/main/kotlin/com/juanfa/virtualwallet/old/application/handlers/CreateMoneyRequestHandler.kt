package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.CreateMoneyRequestCommand
import com.juanfa.virtualwallet.old.domain.model.MoneyRequest
import com.juanfa.virtualwallet.old.domain.model.RequestStatus
import com.juanfa.virtualwallet.old.domain.repository.MoneyRequestRepository
import com.juanfa.virtualwallet.old.domain.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class CreateMoneyRequestHandler (
    private val moneyRequestRepository: MoneyRequestRepository, private val walletRepository: WalletRepository
) {
    fun handle(command: CreateMoneyRequestCommand){
        val wallet = walletRepository.findById(command.wallet)?: throw IllegalArgumentException("Wallet not found")
        if (command.amount <= 0){
            throw IllegalArgumentException("Amount must be greater than 0")
        }
        if(command.sender == command.recipient){
            throw IllegalArgumentException("Sender and recipient cannot be the same")
        }

        val status = try {
            RequestStatus.valueOf(command.status?: RequestStatus.PENDING.name)
        }catch (e: IllegalArgumentException){
            throw IllegalArgumentException("Invalid money request status: ${command.status}")
        }
        val moneyRequest = MoneyRequest(
            id = command.id,
            sender = command.sender,
            recipient = command.recipient,
            amount = command.amount,
            status = status,
            wallet = command.wallet,
            created = command.created,
            update = command.update
        )
        moneyRequestRepository.save(moneyRequest)
    }
}
