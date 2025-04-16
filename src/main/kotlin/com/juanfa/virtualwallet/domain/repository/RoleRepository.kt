package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.Role
import java.util.Optional
import java.util.UUID

interface RoleRepository {
    fun save(role: Role)
    fun findById(id: UUID): Optional<Role>
    fun findByUserAndWallet(userId: UUID, walletId: UUID):Optional<Role>

}