package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.Movement
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MovementRepositoryJPA : JpaRepository<Movement, UUID>