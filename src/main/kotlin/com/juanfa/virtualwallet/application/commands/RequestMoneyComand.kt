package com.juanfa.virtualwallet.application.commands

import java.util.UUID

data class RequestMoneyComand(
    val id: UUID,
    val sender: UUID,
    val recipient: UUID,
    val amount: Int,
    val wallet: UUID
)
