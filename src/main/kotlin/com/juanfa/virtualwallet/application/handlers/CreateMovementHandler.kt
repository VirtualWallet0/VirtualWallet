package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateMovementCommand
import com.juanfa.virtualwallet.domain.model.Movement
import com.juanfa.virtualwallet.domain.repository.MovementRepository
import org.springframework.stereotype.Service

@Service
class CreateMovementHandler(private val movementRepository: MovementRepository) {
    fun handle(command: CreateMovementCommand): Movement {
        if (command.amount <=0){
            throw IllegalArgumentException("Amount must be positive")
        }
        if(command.originWallet == command.destinyWallet){
            throw IllegalArgumentException("Origin and destiny wallets must be different")
        }
        val movement = Movement(
            id = command.id,
            originWallet = command.originWallet,
            destinyWallet = command.destinyWallet,
            amount = command.amount,
            status = command.status,
            startMovementTime = command.startMovementTime,
            startMovementBy = command.startMovementBy,
            approveMovementTime = command.approveMovementTime,
            approveMovementBy = command.approveMovementBy,
            created = command.created,
            update = command.update
        )
        return movementRepository.save(movement)
    }
}
