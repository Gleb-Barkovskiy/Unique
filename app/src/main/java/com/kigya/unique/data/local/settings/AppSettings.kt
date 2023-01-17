package com.kigya.unique.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.ui.tabs.FiltersMapper.toSubgroupList
import com.kigya.unique.ui.tabs.FiltersMapper.toSubgroupBundle
import com.kigya.unique.utils.Quartet
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

    override suspend fun setSubgroupListToDataStore(subgroupList: List<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SUBGROUP_LIST] = subgroupList.toSubgroupBundle()
        }
    }

    override suspend fun setRegularityToDataStore(isAuto: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTO_REGULARITY] = isAuto
        }
    }

    override fun getParamsFromDataStore(): Flow<Quartet<Int, Int, List<String>, Boolean>> =
        dataStore.data.map { preferences ->
            Quartet(
                preferences[PreferencesKeys.COURSE] ?: 1,
                preferences[PreferencesKeys.GROUP] ?: 1,
                preferences[PreferencesKeys.SUBGROUP_LIST]?.toSubgroupList() ?: emptyList(),
                preferences[PreferencesKeys.IS_AUTO_REGULARITY] ?: true
            )
        }
}