package com.juanfa.virtualwallet.old.infrastructure.db

import com.juanfa.virtualwallet.old.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepositoryJPA: JpaRepository<User, UUID>{
    fun findByName(name: String): Optional<User>
}