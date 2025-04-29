package com.juanfa.virtualwallet.old.infrastructure.http.dto

import java.time.LocalDateTime
import java.util.*

data class WalletResponseDTO(
    val id: UUID,
    val name: String,
    val amount: Int,
    val type: String,
    val owner: UUID,
    val created: LocalDateTime,
    val updated: LocalDateTime
)
