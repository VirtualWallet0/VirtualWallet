package com.juanfa.virtualwallet.application.commands

import java.time.LocalDateTime
import java.util.UUID

data class CreateUserCommand (
    val id: UUID,
    val name: String,
    val password: String,
    val created: LocalDateTime,
    val update: LocalDateTime
)