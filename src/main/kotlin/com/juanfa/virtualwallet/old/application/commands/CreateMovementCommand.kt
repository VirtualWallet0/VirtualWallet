package com.juanfa.virtualwallet.old.application.commands

import com.juanfa.virtualwallet.old.domain.model.MovementStatus
import java.time.LocalDateTime
import java.util.*

data class CreateMovementCommand(
    val id: UUID,
    val originWallet: UUID,
    val destinyWallet: UUID,
    val amount: Int,
    val status: MovementStatus,
    val startMovementTime: LocalDateTime,
    val startMovementBy: UUID?,
    val approveMovementTime: LocalDateTime?,
    val approveMovementBy: UUID?,
    val created: LocalDateTime,
    val update: LocalDateTime

    )
