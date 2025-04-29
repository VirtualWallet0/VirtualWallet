package com.juanfa.virtualwallet.old.domain.repository

import com.juanfa.virtualwallet.old.domain.model.Movement
import java.util.*

interface MovementRepository {
    fun save(movement: Movement): Movement
    fun findById(id: UUID): Optional<Movement>
}