package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.MoneyRequest
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MoneyRequestRepositoryJPA : JpaRepository<MoneyRequest, UUID> {
    override fun findById(id: UUID): Optional<MoneyRequest>
}