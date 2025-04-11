package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

enum class RoleType{
    ADMIN, OPERATOR
}
/*
data class Role (
    val id: UUID,
    val user: UUID,
    val wallet: UUID,
    val roleType: RoleType,
    val assign: LocalDateTime = LocalDateTime.now()
)*/

@Entity
@Table(name = "roles")
data class Role (
    @Id
    val id: UUID,

    @Column(nullable = false)
    val user: UUID,

    @Column(nullable = false)
    val wallet: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val roleType: RoleType,

    @Column(nullable = false)
    val assign: LocalDateTime = LocalDateTime.now()
)