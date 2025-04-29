package com.juanfa.virtualwallet.old.domain.repository

import com.juanfa.virtualwallet.old.domain.model.MoneyRequest
import java.util.Optional
import java.util.UUID

interface MoneyRequestRepository {
    fun save(moneyRequest: MoneyRequest): MoneyRequest
    fun findById(id: UUID): Optional<MoneyRequest>
}