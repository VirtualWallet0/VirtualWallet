package com.juanfa.virtualwallet.application.handlers

import com.juanfa.virtualwallet.application.commands.CreateUserCommand
import com.juanfa.virtualwallet.domain.model.User
import com.juanfa.virtualwallet.infrastructure.db.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserHandler (private val userRepository: UserRepository) {
    fun handle(command: CreateUserCommand): User {
        if (command.name.isBlank()){
            throw IllegalArgumentException("Name cannot be blank")
        }
        val user = User(
            id = command.id,
            name = command.name,
            password = command.password,
            created = command.created,
            update = command.update
        )
        return userRepository.save(user)

    }

}