package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.Role
import com.juanfa.virtualwallet.domain.model.RoleEntity
import com.juanfa.virtualwallet.domain.repository.RoleRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RoleRepositoryImpl(private val jpaRepository: RoleRepositoryJPA) : RoleRepository {

    override fun save(role: Role) {
        jpaRepository.save(role.toEntity())
    }

    override fun findById(id: UUID): Optional<Role> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }
    override fun findByUserAndWallet(userId: UUID, walletId: UUID): Optional<Role>{
        return jpaRepository.findByUserAndWallet(userId, walletId).map {it.toDomain()}
    }

    private fun Role.toEntity() = RoleEntity(
        id = id,
        user = user,
        wallet = wallet,
        roleType = roleType,
        assign = assign
    )

    private fun RoleEntity.toDomain() = Role(
        id = id,
        user = user,
        wallet = wallet,
        roleType = roleType,
        assign = assign
    )
}