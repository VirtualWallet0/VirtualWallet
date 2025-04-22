package com.juanfa.virtualwallet.application.commands

import java.util.UUID

data class LoginUserCommand (
    val userId: UUID,
    val password: String
)
