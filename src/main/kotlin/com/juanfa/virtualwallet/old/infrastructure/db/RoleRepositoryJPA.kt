package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleRepositoryJPA : JpaRepository<RoleEntity, UUID>{
fun findByUserAndWallet(user: UUID, wallet: UUID): Optional<RoleEntity>}
