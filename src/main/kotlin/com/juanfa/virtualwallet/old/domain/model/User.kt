package com.juanfa.virtualwallet.old.domain.model
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

/*package com.juanfa.virtualwallet.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class User (
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val password: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val update: LocalDateTime = LocalDateTime.now()
)*/

@Entity
@Table(name = "users")
data class User (
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val update: LocalDateTime = LocalDateTime.now()
)