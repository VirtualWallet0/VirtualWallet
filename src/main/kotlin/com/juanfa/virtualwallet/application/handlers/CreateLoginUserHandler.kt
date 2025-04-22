package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.LoginUserCommand
import com.juanfa.virtualwallet.domain.model.LoginHistory
import com.juanfa.virtualwallet.domain.repository.LoginHistoryRepository
import com.juanfa.virtualwallet.domain.repository.UserRepository
import java.util.*

class CreateLoginUserHandler (private val userRepository: UserRepository, private val loginHistoryRepository: LoginHistoryRepository) {
    fun handle(command: LoginUserCommand) {
        val user = userRepository.findById(command.userId).orElseThrow(){
            IllegalArgumentException("User does not exist")
        }
        val success = user.password == command.password

        val loginHistory = LoginHistory(
            id = UUID.randomUUID(),
            user = command.userId,
            success = success
        )
        loginHistoryRepository.save(loginHistory)

        if(!success){
            throw IllegalArgumentException("Incorrect username or password")
        }
    }
}