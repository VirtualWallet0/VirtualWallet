package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateLoginHistoryCommand
import com.juanfa.virtualwallet.domain.model.LoginHistory
import com.juanfa.virtualwallet.domain.repository.LoginHistoryRepository
import com.juanfa.virtualwallet.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CreateLoginHistoryHandler (private val loginHistoryRepository: LoginHistoryRepository, private val userRepository: UserRepository) {
    fun handle(command: CreateLoginHistoryCommand){

        if(!userRepository.findById(command.user).isPresent) {
            throw IllegalArgumentException("User does not exist")
            }
        if(loginHistoryRepository.findById(command.id).isPresent) {
            throw IllegalArgumentException("Login History with this ID already exists")
        }



        val loginHistory = LoginHistory(
            id = command.id,
            user = command.user,
            //dateTime = command.dateTime,
            //dateTime = LocalDateTime.now(),
            dateTime = command.dateTime ?: LocalDateTime.now(),
            success = command.success
        )
        loginHistoryRepository.save(loginHistory)
    }
}