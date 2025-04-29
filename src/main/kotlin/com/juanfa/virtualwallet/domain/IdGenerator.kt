package com.juanfa.virtualwallet.domain

import java.util.*

data class Id(val value: UUID) {

    companion object {
        fun from(value: String): Id = Id(UUID.fromString(value))

        fun generate(): UUID = UUID.randomUUID()
    }
}