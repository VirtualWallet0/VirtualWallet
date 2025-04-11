package com.juanfa.virtualwallet.application.commands

import java.util.*

data class InitialMovementCommand(
    val id: UUID,
    val originWallet: UUID,
    val destinyWallet: UUID,
    val amount: Int

    )
