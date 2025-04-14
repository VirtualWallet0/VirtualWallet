package com.juanfa.virtualwallet.infrastructure.db

import com.juanfa.virtualwallet.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepositoryJPA: JpaRepository<User, UUID>