package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.Movement
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MovementRepositoryJPA : JpaRepository<Movement, UUID>