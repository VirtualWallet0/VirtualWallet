package com.juanfa.virtualwallet.infrastructure.http.dto

data class CreateWalletRequestDTO(
    val name: String,
    val type: String,
    val initialAmount: Int
)