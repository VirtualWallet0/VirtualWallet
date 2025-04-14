package com.juanfa.virtualwallet.domain.model

import jakarta.persistence.*
import java.util.UUID

enum class UserActionType{
    Admin, Operator
}
/*data class UserRoleAction (
    val id: UUID,
    val user: UUID,
    val type: UserActionType
)*/

@Entity
@Table(name = "user_role_actions")
data class UserRole (
    @Id
    val id: UUID,

    @Column(nullable = false)
    val user: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: UserActionType
)
