package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.MovementHistory
import java.util.UUID

interface MovementHistoryRepository {
    fun save(movementHistory: MovementHistory): MovementHistory
    fun findByMovement(movementId: UUID): List<MovementHistory>
}