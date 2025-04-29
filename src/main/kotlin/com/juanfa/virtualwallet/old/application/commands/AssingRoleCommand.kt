package com.juanfa.virtualwallet.old.application.commands

import java.util.UUID

data class AssingRoleCommand(
    val id: UUID,
    val user: UUID,
    val wallet: UUID,
    val roleType: String
)
