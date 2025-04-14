package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.Movement
import com.juanfa.virtualwallet.domain.repository.MovementRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MovementRepositoryImpl( private val jpaRepository: MovementRepositoryJPA) : MovementRepository {
    override fun save(movement: Movement): Movement {
        return jpaRepository.save(movement)
    }

    override fun findById(id: UUID): Optional<Movement> {
        return jpaRepository.findById(id)
    }
}