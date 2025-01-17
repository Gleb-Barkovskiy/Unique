package com.kigya.unique.data.local.settings

import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.utils.Quartet
import kotlinx.coroutines.flow.Flow

interface AppSettingsSource {

    fun isSignedIn(): Flow<Boolean>

    suspend fun signIn()

    suspend fun setCurrentAccountType(accountType: AccountType)

    fun getCurrentAccountType(): Flow<AccountType>

    fun getTeacherFromDataStore(): Flow<String>

    suspend fun setCourseToDataStore(course: Int)

    suspend fun setGroupToDataStore(group: Int)

    suspend fun setSubgroupListToDataStore(subgroupList: List<String>)

    suspend fun setRegularityToDataStore(isAuto: Boolean)

    suspend fun setTeacherToDataStore(teacher: String)

    fun getParamsFromDataStore(): Flow<Quartet<Int, Int, List<String>, Boolean>>
    fun isListAnimationEnabled(): Flow<Boolean>
    suspend fun setListAnimationEnabled(isEnabled: Boolean)
}
