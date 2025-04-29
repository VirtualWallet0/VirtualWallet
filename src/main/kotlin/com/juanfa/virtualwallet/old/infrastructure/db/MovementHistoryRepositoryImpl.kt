package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.MovementHistory
import com.juanfa.virtualwallet.old.domain.repository.MovementHistoryRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MovementHistoryRepositoryImpl (private val jpaRepository: MovementHistoryRepositoryJPA): MovementHistoryRepository {
    override fun save(movementHistory: MovementHistory): MovementHistory{
        return jpaRepository.save(movementHistory)
    }

    override fun findByMovement(movementId: UUID): List<MovementHistory> {
        return jpaRepository.findByMovement(movementId)
    }
    override fun findByOriginWalletAndDestinyWallet(originWallet: UUID, destinyWallet: UUID): List<MovementHistory> {
        return jpaRepository.findByOriginWalletAndDestinyWallet(originWallet, destinyWallet)
    }
}