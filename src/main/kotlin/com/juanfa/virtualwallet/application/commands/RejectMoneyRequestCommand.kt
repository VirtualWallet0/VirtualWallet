package com.juanfa.virtualwallet.application.commands

import java.util.UUID

data class RejectMoneyRequestCommand(
    val requestId: UUID,
    val rejectedBy: UUID,
)
