package com.juanfa.virtualwallet.application.commands

import com.juanfa.virtualwallet.domain.model.WalletType
import java.time.LocalDateTime
import java.util.UUID

data class CreateWalletCommand (
    val id: UUID,
    val name: String,
    val type: String,
    val owner: UUID,
    val initialAmount: Int,
    val create: LocalDateTime,
    val update: LocalDateTime
)