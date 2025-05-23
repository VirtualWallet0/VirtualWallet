package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.User
import com.juanfa.virtualwallet.domain.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryImpl (private val userRepositoryJPA: UserRepositoryJPA): UserRepository {
    override fun save(user: User): User {
        return userRepositoryJPA.save(user)
    }

    override fun findById(id: UUID): Optional<User> {
        return userRepositoryJPA.findById(id)
    }
    override fun findByName(name: String): Optional<User> {
        return userRepositoryJPA.findByName(name)
    }
}
