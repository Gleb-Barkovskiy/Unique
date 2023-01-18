package com.kigya.unique.utils.extensions.specific.account

import com.kigya.unique.data.dto.account.AccountType

fun String?.mapToAccountType() = when (this) {
    AccountType.STUDENT.name -> AccountType.STUDENT
    AccountType.TEACHER.name -> AccountType.TEACHER
    else -> AccountType.STUDENT
}

fun AccountType?.mapToString() = when (this) {
    AccountType.STUDENT -> name
    AccountType.TEACHER -> name
    else -> AccountType.STUDENT.name
}