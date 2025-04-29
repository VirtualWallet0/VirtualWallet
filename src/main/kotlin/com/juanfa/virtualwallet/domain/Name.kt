package com.juanfa.virtualwallet.domain

data class Name(val name: String){
    init {
        if (name.isBlank())
            throw InvalidNameException("Name should not be blac")
    }
}