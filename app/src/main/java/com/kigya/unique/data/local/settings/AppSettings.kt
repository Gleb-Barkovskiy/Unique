package com.kigya.unique.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Tuple4
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.utils.constants.PreferencesKeys
import com.kigya.unique.utils.extensions.mapToAccountType
import com.kigya.unique.utils.extensions.mapToString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppSettingsSource {

    override fun isSignedIn(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.IS_SIGNED_IN] ?: false
        }

    override suspend fun signIn() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SIGNED_IN] = true
        }
    }

    override suspend fun setCurrentAccountType(accountType: AccountType) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCOUNT_TYPE] = accountType.mapToString()
        }
    }

    override fun getCurrentAccountType(): Flow<AccountType> =
        dataStore.data.map {
            it[PreferencesKeys.ACCOUNT_TYPE]?.mapToAccountType() ?: AccountType.STUDENT
        }

    override suspend fun setCourseToDataStore(course: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COURSE] = course
        }
    }

    override suspend fun setGroupToDataStore(group: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.GROUP] = group
        }
    }

    override suspend fun setSubgroupToDataStore(subgroup: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SUBGROUP] = subgroup
        }
    }

    override suspend fun setRegularityToDataStore(regularity: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.REGULARITY] = regularity
        }
    }

    override fun getParamsFromDataStore(): Flow<Tuple4<Int, Int, String?, String?>> =
        dataStore.data.map {
            Tuple4(
                it[PreferencesKeys.COURSE] ?: 1,
                it[PreferencesKeys.GROUP] ?: 1,
                it[PreferencesKeys.SUBGROUP],
                it[PreferencesKeys.REGULARITY],
            )
        }

}