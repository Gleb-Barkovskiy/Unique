package com.kigya.unique.data.local.settings

import arrow.core.Tuple4
import com.kigya.unique.data.dto.account.AccountType
import kotlinx.coroutines.flow.Flow

interface AppSettingsSource {

    fun isSignedIn(): Flow<Boolean>

    suspend fun signIn()

    suspend fun setCurrentAccountType(accountType: AccountType)

    fun getCurrentAccountType(): Flow<AccountType>

    suspend fun setCourseToDataStore(course: Int)

    suspend fun setGroupToDataStore(group: Int)

    suspend fun setSubgroupToDataStore(subgroup: String)

    suspend fun setRegularityToDataStore(regularity: String)

    fun getParamsFromDataStore(): Flow<Tuple4<Int, Int, String?, String?>>

}