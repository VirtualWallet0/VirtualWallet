package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.Wallet
import com.juanfa.virtualwallet.domain.repository.WalletRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class WalletRepositoryImpl (private val JpaRepository: WalletRepositoryJPA): WalletRepository {
    override fun save (wallet: Wallet){
        JpaRepository.save(wallet)
    }

    override fun findById(id: UUID): Wallet {
        return JpaRepository.findById(id).orElse(null)
    }
}
