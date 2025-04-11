package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.Wallet
import java.util.UUID

interface WalletRepository {
    fun save (wallet: Wallet)
    fun findById(id: UUID): Wallet
}