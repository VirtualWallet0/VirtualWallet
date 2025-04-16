package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "roles")
data class RoleEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id",nullable = false)
    val user: UUID,

    @Column(nullable = false)
    val wallet: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val roleType: RoleType,

    @Column(nullable = false)
    val assign: LocalDateTime
)