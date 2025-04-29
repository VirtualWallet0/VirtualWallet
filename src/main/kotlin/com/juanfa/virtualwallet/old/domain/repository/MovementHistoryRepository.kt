package com.juanfa.virtualwallet.old.domain.repository

import com.juanfa.virtualwallet.old.domain.model.MovementHistory
import java.util.UUID

interface MovementHistoryRepository {
    fun save(movementHistory: MovementHistory): MovementHistory
    fun findByMovement(movementId: UUID): List<MovementHistory>
    fun findByOriginWalletAndDestinyWallet(originWallet: UUID, destinyWallet: UUID): List<MovementHistory>
}