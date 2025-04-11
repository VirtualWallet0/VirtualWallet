package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateWalletCommand
import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.model.WalletType
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class CreateWalletHandler (private val walletRepository: WalletRepository) {

    fun handle (command: CreateWalletCommand) {
        val type = WalletType.valueOf(command.type.uppercase())
        val wallet = Wallet(
            id = command.id,
            name = command.name,
            amount = command.initialAmount,
            type = type,
            owner = command.owner,
            created = command.create,
            update = command.create
        )
        walletRepository.save(wallet)
    }

}