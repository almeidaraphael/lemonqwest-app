package com.lemonqwest.app.domain.user

class UserRepositoryException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause)
