package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateRoleCommand
import com.juanfa.virtualwallet.domain.model.Role
import com.juanfa.virtualwallet.domain.repository.RoleRepository
import com.juanfa.virtualwallet.domain.repository.UserRepository
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateRoleHandler(private val roleRepository: RoleRepository, private val userRepository: UserRepository, private val walletRepository: WalletRepository) {

    fun handle(command: CreateRoleCommand) {
        if (command.user.toString().isBlank() || command.wallet.toString().isBlank() ||
            command.user == UUID.fromString("00000000-0000-0000-0000-000000000000") ||
            command.wallet == UUID.fromString("00000000-0000-0000-0000-000000000000")
        ) {
            throw IllegalArgumentException("User or Wallet ID is invalid")
        }
        val userExist = userRepository.findById(command.user).isPresent
        if(!userExist){
            throw IllegalArgumentException("The user does not exist")
        }
        val wallet = walletRepository.findById(command.wallet)
        if(wallet == null) {
            throw IllegalArgumentException("Wallet does not exist")
        }
        val existing = roleRepository.findByUserAndWallet(command.user, command.wallet)
        if(existing .isPresent){
            throw IllegalArgumentException("Role already exists for this user and wallet")
        }

        val role = Role(
            id = command.id,
            user = command.user,
            wallet = command.wallet,
            roleType = command.roleType,
            assign = command.assign
        )
        roleRepository.save(role)
    }

}