package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.MoneyRequest
import java.util.Optional
import java.util.UUID

interface MoneyRequestRepository {
    fun save(moneyRequest: MoneyRequest): MoneyRequest
    fun findById(id: UUID): Optional<MoneyRequest>
}