package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WalletRepositoryJPA: JpaRepository<Wallet, UUID>{
    fun findByOwner(ownerId: UUID): List<Wallet>
}
