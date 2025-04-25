package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateWalletCommand
import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.model.WalletType
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CreateWalletHandler (private val walletRepository: WalletRepository) {

    fun handle (command: CreateWalletCommand): Wallet {
        if (command.initialAmount < 0){
            throw IllegalArgumentException("Amount cannot be negative")
        }
        if (command.name.isBlank()){
            throw IllegalArgumentException("Wallet name cannot be blank")
        }
        val existingWallet = walletRepository.findById(command.id)
        if (existingWallet != null){
            throw IllegalArgumentException("Wallet whit ID ${command.id} already exists")
        }
        try {
            val type = WalletType.valueOf(command.type.uppercase())
            val currentDateTime = LocalDateTime.now()

            val wallet = Wallet(
                id = command.id,
                name = command.name,
                amount = command.initialAmount,
                type = type,
                owner = command.owner,
                created = currentDateTime,
                update = currentDateTime
            )
            return walletRepository.save(wallet)

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid wallet type: ${command.type}")
        }

    }

}