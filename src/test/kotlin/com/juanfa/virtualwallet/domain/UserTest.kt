package com.juanfa.virtualwallet.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserTest {

    @Test
    fun testUserNameIsValidOne() {
        // Should have a valid id
        assertThrows<InvalidDomainObjectException> {
            User.from("", "kaka kakaka", "email@email.com")
        }
        // Should have a valid name
        assertThrows<InvalidDomainObjectException> {
            User.from("kk", "", "email@email.com")
        }
    }
}