package com.juanfa.virtualwallet.old.application.handlers

import com.juanfa.virtualwallet.old.application.commands.LoginUserCommand
import com.juanfa.virtualwallet.old.domain.model.LoginHistory
import com.juanfa.virtualwallet.old.domain.repository.LoginHistoryRepository
import com.juanfa.virtualwallet.old.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateLoginUserHandler(
    private val userRepository: UserRepository,
    private val loginHistoryRepository: LoginHistoryRepository
) {
    fun handle(command: LoginUserCommand) {
        val user = userRepository.findById(command.userId).orElseThrow() {
            IllegalArgumentException("User does not exist")
        }
        val success = user.password == command.password

        val loginHistory = LoginHistory(
            id = UUID.randomUUID(),
            user = command.userId,
            success = success
        )
        loginHistoryRepository.save(loginHistory)

        if (!success) {
            throw IllegalArgumentException("Incorrect username or password")
        }
    }
}