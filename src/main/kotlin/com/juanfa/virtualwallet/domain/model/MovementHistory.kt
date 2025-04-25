package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/*
data class MovementHistory (
    val id: UUID,
    val originWallet: UUID,
    val destinyWallet: UUID,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val success: Boolean,
    val movement: UUID,
    val failureReason: String? = null
)*/

@Entity
@Table(name = "movement_histories")
data class MovementHistory (
    @Id
    val id: UUID,

    @Column(nullable = false)
    val originWallet: UUID,

    @Column(nullable = false)
    val destinyWallet: UUID,

    @Column(nullable = false)
    val dateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val success: Boolean,

    @Column(nullable = true)
    val movement: UUID?,

    @Column
    val failureReason: String? = null
)