package com.juanfa.virtualwallet.domain

open class InvalidDomainObjectException(override val message: String): RuntimeException(message)


class InvalidUserException(override val message: String) : InvalidDomainObjectException(message)
class InvalidNameException(override val message: String) : InvalidDomainObjectException(message)