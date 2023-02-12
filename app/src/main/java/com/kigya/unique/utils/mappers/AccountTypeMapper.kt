package com.kigya.unique.utils.mappers

import com.kigya.unique.data.dto.account.AccountType

object AccountTypeMapper {
    fun mapToAccountType(str: String) = when (str) {
        AccountType.STUDENT.name -> AccountType.STUDENT
        AccountType.TEACHER.name -> AccountType.TEACHER
        else -> AccountType.STUDENT
    }

    fun mapToString(accountType: AccountType) = accountType.name
}
