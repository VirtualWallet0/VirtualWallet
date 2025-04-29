package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.MoneyRequest
import com.juanfa.virtualwallet.old.domain.repository.MoneyRequestRepository
import org.springframework.stereotype.Repository
import java.util.*
@Repository
class MoneyRequestRepositoryImpl (private val jpaRepository: MoneyRequestRepositoryJPA): MoneyRequestRepository{
    override fun save(moneyRequest: MoneyRequest): MoneyRequest{
        return jpaRepository.save(moneyRequest)
    }

    override fun findById(id: UUID): Optional<MoneyRequest> {
        return jpaRepository.findById(id)
    }
}