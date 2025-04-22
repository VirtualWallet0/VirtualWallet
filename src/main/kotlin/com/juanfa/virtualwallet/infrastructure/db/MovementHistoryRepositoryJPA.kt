package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.MovementHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MovementHistoryRepositoryJPA : JpaRepository<MovementHistory, UUID> {
    fun findByMovement(movement: UUID): List<MovementHistory>
}