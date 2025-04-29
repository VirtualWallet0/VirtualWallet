package com.juanfa.virtualwallet.old.domain.repository

import com.juanfa.virtualwallet.old.domain.model.LoginHistory
import java.util.*

interface LoginHistoryRepository {
    fun save(loginHistory: LoginHistory)
    fun findByUser(userId: UUID): List<LoginHistory>
    fun findById(id: UUID): Optional<LoginHistory>
}

/*
import com.juanfa.virtualwallet.domain.model.User
import com.juanfa.virtualwallet.domain.model.LoginHistory
import java.util.*

class LoginHistoryRepository {
    private val loginHistories = mutableListOf<LoginHistory>()

    fun save(loginHistory: LoginHistory) {
        loginHistories.add(loginHistory)
    }

    fun findByUser(user: User): List<LoginHistory> {
        return loginHistories.filter { it.user == user.id }
    }

    fun findById(id: UUID): Optional<LoginHistory> {
        val loginHistory = loginHistories.find { it.id == id }
        return Optional.ofNullable(loginHistory)
    }
}*/
