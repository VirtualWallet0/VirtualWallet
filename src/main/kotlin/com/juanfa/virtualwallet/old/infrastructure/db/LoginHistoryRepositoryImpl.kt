package com.juanfa.virtualwallet.old.infrastructure.db

/*import com.juanfa.virtualwallet.domain.model.LoginHistory
import com.juanfa.virtualwallet.domain.repository.LoginHistoryRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LoginHistoryRepositoryImpl(private val jpaRepository: LoginHistoryRepositoryJPA) : LoginHistoryRepository {
    override fun save(loginHistory: LoginHistory) {
        jpaRepository.save(loginHistory.toEntity())
    }

    override fun findByUser(userId: UUID): List<LoginHistory> {
        return jpaRepository.findByUser(userId).map { it.toDomain() }
    }
    override fun findById(id: UUID): Optional<LoginHistory> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    private fun LoginHistory.toEntity() = LoginHistory(
        id = id,
        user = user,
        dateTime = dateTime,
        success = success
    )

    private fun LoginHistory.toDomain() = LoginHistory(
        id = id,
        user = user,
        dateTime = dateTime,
        success = success
    )
}*/

import com.juanfa.virtualwallet.old.domain.model.LoginHistory
import com.juanfa.virtualwallet.old.domain.repository.LoginHistoryRepository
import com.juanfa.virtualwallet.old.infrastructure.db.entity.LoginHistoryEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LoginHistoryRepositoryImpl(private val jpaRepository: LoginHistoryRepositoryJPA) : LoginHistoryRepository {
    override fun save(loginHistory: LoginHistory) {
        jpaRepository.save(loginHistory.toEntity())
    }

    override fun findByUser(userId: UUID): List<LoginHistory> {
        return jpaRepository.findByUser(userId).map { it.toDomain() }
    }

    override fun findById(id: UUID): Optional<LoginHistory> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    private fun LoginHistory.toEntity() = LoginHistoryEntity(
        id = id,
        user = user,
        dateTime = dateTime,
        success = success
    )

    private fun LoginHistoryEntity.toDomain() = LoginHistory(
        id = id,
        user = user,
        dateTime = dateTime,
        success = success
    )
}