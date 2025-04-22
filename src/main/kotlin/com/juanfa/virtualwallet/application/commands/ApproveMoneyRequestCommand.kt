package com.juanfa.virtualwallet.application.commands

import java.util.UUID

data class ApproveMoneyRequestCommand(
    val requestId: UUID,
    val approvedBy: UUID
)