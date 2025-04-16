package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.MoneyRequest
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface MoneyRequestRepositoryJPA : JpaRepository<MoneyRequest, UUID>