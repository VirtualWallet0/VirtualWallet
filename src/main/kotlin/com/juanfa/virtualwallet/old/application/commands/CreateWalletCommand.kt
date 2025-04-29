package com.juanfa.virtualwallet.old.application.commands

import java.time.LocalDateTime
import java.util.*

data class CreateWalletCommand(
    val id: UUID,
    val name: String,
    val type: String,
    val owner: UUID,
    val initialAmount: Int,
    val create: LocalDateTime,
    val update: LocalDateTime
)