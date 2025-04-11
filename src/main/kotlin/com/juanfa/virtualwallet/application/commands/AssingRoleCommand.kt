package com.juanfa.virtualwallet.application.commands

import java.util.UUID

data class AssingRoleCommand(
    val id: UUID,
    val user: UUID,
    val wallet: UUID,
    val roleType: String
)
