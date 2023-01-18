package com.kigya.unique.utils.exceptions

open class AppException(cause: Throwable) : RuntimeException(cause)

class ConnectionException(cause: Throwable) : AppException(cause = cause)