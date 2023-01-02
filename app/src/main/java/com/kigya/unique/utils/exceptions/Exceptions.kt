package com.kigya.unique.utils.exceptions


open class AppException(cause: Throwable) : RuntimeException(cause)

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

