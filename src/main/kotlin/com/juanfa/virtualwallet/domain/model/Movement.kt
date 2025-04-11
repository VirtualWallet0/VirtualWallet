package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

enum class MovementStatus{
    Pending, Approved, Rejected
}
/*
data class Movement (
    val id: UUID = UUID.randomUUID(),
    val originWallet: UUID,
    val destinyWallet: UUID,
    val amount: Int,
    val status: MovementStatus = MovementStatus.Pending,
    val startMovement: UUID,
    val approveMovement: UUID,
    val created: LocalDateTime = LocalDateTime.now(),
    val update: LocalDateTime = LocalDateTime.now()
)*/
@Entity
@Table(name = "movements")
data class Movement (
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val originWallet: UUID,

    @Column(nullable = false)
    val destinyWallet: UUID,

    @Column(nullable = false)
    val amount: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: MovementStatus = MovementStatus.Pending,

    @Column(nullable = false)
    val startMovement: UUID,

    @Column(nullable = false)
    val approveMovement: UUID,

    @Column(nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val update: LocalDateTime = LocalDateTime.now()
)