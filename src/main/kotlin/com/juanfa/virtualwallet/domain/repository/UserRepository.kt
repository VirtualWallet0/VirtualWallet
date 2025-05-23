package com.juanfa.virtualwallet.domain.repository

import com.juanfa.virtualwallet.domain.model.User
import java.util.*

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): Optional<User>
    fun findByName(name: String): Optional<User>

}