package com.juanfa.virtualwallet.old.application.commands

import com.juanfa.virtualwallet.old.domain.model.RequestStatus
import java.time.LocalDateTime
import java.util.UUID

data class CreateMoneyRequestCommand(
    val id: UUID,
    val sender: UUID,
    val recipient: UUID,
    val amount: Int,
    val status: String? = RequestStatus.PENDING.name,
    val wallet: UUID,
    val created: LocalDateTime = LocalDateTime.now(),
    val update: LocalDateTime = LocalDateTime.now()
)
