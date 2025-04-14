package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class WalletRepositoryImpl (private val walletRepositoryJPA: WalletRepositoryJPA): WalletRepository {
    override fun save (wallet: Wallet): Wallet{
        return walletRepositoryJPA.save(wallet)
    }

    override fun findById(id: UUID): Wallet? {
        return walletRepositoryJPA.findById(id).orElse(null)
    }
}
