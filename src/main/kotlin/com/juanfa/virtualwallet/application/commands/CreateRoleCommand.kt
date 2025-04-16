package com.juanfa.virtualwallet.application.commands

import com.juanfa.virtualwallet.domain.model.RoleType
import java.time.LocalDateTime
import java.util.UUID

data class CreateRoleCommand(
    val id: UUID,
    val user: UUID,
    val wallet: UUID,
    val roleType: RoleType,
    val assign: LocalDateTime
)