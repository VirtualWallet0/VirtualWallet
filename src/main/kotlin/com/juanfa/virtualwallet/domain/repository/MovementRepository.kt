package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.Movement
import java.util.*

interface MovementRepository {
    fun save(movement: Movement): Movement
    fun findById(id: UUID): Optional<Movement>
}