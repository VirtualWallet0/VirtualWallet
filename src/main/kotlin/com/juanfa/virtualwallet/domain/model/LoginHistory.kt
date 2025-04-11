package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/*
data class LoginHistory (
    val id: UUID,
    val user: UUID,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val success: Boolean
)*/
@Entity
@Table(name = "login_histories")
data class LoginHistory (
    @Id
    val id: UUID,

    @Column(nullable = false)
    val user: UUID,

    @Column(nullable = false)
    val dateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val success: Boolean
)