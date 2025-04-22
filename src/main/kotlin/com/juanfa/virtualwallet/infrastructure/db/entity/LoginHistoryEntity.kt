package com.juanfa.virtualwallet.infrastructure.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "login_histories")
data class LoginHistoryEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val user: UUID,

    @Column(nullable = false)
    val dateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val success: Boolean
)
