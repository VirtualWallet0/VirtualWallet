package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.Wallet
import com.juanfa.virtualwallet.old.domain.repository.WalletRepository
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
    override fun findByOwner(ownerId: UUID): List<Wallet> {
        return walletRepositoryJPA.findByOwner(ownerId)
    }
}
