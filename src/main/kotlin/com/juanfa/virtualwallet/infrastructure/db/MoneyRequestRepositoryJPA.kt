package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.MoneyRequest
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MoneyRequestRepositoryJPA : JpaRepository<MoneyRequest, UUID> {
    override fun findById(id: UUID): Optional<MoneyRequest>
}