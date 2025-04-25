package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.Wallet
import org.springframework.stereotype.Repository
import java.util.UUID

interface WalletRepository {
    fun save (wallet: Wallet): Wallet
    fun findById(id: UUID): Wallet?
    fun findByOwner(ownerId: UUID): List<Wallet>
}