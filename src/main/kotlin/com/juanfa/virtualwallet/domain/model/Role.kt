package com.juanfa.virtualwallet.domain.model

import java.time.LocalDateTime
import java.util.UUID

enum class RoleType {
    ADMIN, OPERATOR
}

data class Role(
    val id: UUID,
    val user: UUID,
    val wallet: UUID,
    val roleType: RoleType,
    val assign: LocalDateTime = LocalDateTime.now()
)