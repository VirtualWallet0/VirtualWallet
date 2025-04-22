package com.juanfa.virtualwallet.infrastructure.db

/*import com.juanfa.virtualwallet.domain.model.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LoginHistoryRepositoryJPA : JpaRepository <LoginHistory, UUID> {
    fun findByUser(user: UUID): List<LoginHistory>

}*/

import com.juanfa.virtualwallet.infrastructure.db.entity.LoginHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LoginHistoryRepositoryJPA : JpaRepository<LoginHistoryEntity, UUID> {
    fun findByUser(userId: UUID): List<LoginHistoryEntity>
}
