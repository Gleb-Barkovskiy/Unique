package com.kigya.unique.utils.exceptions


open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class AuthException(
    cause: Throwable
) : AppException(cause = cause)

class ConnectionException(cause: Throwable) : AppException(cause = cause)

internal inline fun <T> wrapBackendExceptions(block: () -> T): T {
    try {
        return block.invoke()
    } catch (e: AppException) {
        if (e is AuthException) throw AuthException(e)
        else throw e
    }
}

