package com.juanfa.virtualwallet.application.commands

import java.time.LocalDateTime
import java.util.UUID

data class CreateLoginHistoryCommand(
    val id: UUID,
    val user: UUID,
    val success: Boolean,
    //val dateTime: LocalDateTime = LocalDateTime.now()
    val dateTime: LocalDateTime? = null
)
