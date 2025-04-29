package com.juanfa.virtualwallet.old.infrastructure.http.dto

data class CreateWalletRequestDTO(
    val name: String,
    val type: String,
    val initialAmount: Int
)