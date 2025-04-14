package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.User
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
}
