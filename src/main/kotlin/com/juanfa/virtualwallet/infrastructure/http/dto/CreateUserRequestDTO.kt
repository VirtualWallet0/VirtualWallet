package com.juanfa.virtualwallet.infrastructure.http.dto


data class CreateUserRequestDTO(
    val name: String,
    val password: String
)