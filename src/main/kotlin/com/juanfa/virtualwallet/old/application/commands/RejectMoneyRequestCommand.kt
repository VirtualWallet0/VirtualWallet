package com.juanfa.virtualwallet.old.application.commands

import java.util.UUID

data class RejectMoneyRequestCommand(
    val requestId: UUID,
    val rejectedBy: UUID,
)
