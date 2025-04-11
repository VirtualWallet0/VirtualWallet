package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

enum class RequestStatus{
    Pending, Approved, Rejected
}
/*
data class MoneyRequest (
    val id: UUID = UUID.randomUUID(),
    val sender: UUID,
    val recipient: UUID,
    val amount: Int,
    val status: RequestStatus = RequestStatus.Pending,
    val wallet: UUID,
    val created: LocalDateTime = LocalDateTime.now(),
    val update: LocalDateTime = LocalDateTime.now()
)*/
@Entity
@Table(name = "money_requests")
data class MoneyRequest (
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val sender: UUID,

    @Column(nullable = false)
    val recipient: UUID,

    @Column(nullable = false)
    val amount: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: RequestStatus = RequestStatus.Pending,

    @Column(nullable = false)
    val wallet: UUID,

    @Column(nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val update: LocalDateTime = LocalDateTime.now()
)