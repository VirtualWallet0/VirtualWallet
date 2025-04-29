package com.juanfa.virtualwallet.domain

data class User(
    val id: Id,
    val name: Name,
    val email: String,
    val loginHistory: Set<UserLoginHistory>
) {
    init {
        if (id.isBlank())
            throw InvalidUserException("Id should not be empty")
    }

    companion object {
        fun from(id: String, name: String, email: String) = User(
            id = id,
            name = Name(name),
            email = email
        )
    }
}

