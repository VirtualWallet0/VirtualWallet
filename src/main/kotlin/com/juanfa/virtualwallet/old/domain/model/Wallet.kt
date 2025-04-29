/*
package com.juanfa.virtualwallet.domain.model

import java.time.LocalDateTime
import java.util.UUID

enum class WalletType{
    Company, Personal
}
data class Wallet (
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: Int,
    val type: WalletType,
    val owner: UUID,
    val created: LocalDateTime = LocalDateTime.now(),
    val update: LocalDateTime = LocalDateTime.now()
)
*/

package com.juanfa.virtualwallet.old.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID



enum class WalletType {
    COMPANY, PERSONAL
}

@Entity
@Table(name = "wallets")
data class Wallet (
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val amount: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: WalletType,

    @Column(nullable = false)
    val owner: UUID,

    @Column(nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val update: LocalDateTime = LocalDateTime.now()
)