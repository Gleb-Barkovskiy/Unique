package com.kigya.unique.data.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.utils.Quartet
import com.kigya.unique.utils.constants.ModelConst.DEFAULT_SUBGROUPS_VALUE
import com.kigya.unique.utils.mappers.AccountTypeMapper
import com.kigya.unique.utils.mappers.FiltersMapper.getSubgroupBundle
import com.kigya.unique.utils.mappers.FiltersMapper.getSubgroupList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(
    private val dataStore: DataStore<Preferences>,
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
            preferences[PreferencesKeys.ACCOUNT_TYPE] = AccountTypeMapper.mapToString(accountType)
        }
    }

    override fun getCurrentAccountType(): Flow<AccountType> =
        dataStore.data.map {
            AccountTypeMapper.mapToAccountType(
                it[PreferencesKeys.ACCOUNT_TYPE] ?: STUDENT_STRING_NAME,
            )
        }

    override fun getTeacherFromDataStore(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.TEACHER] ?: ""
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
            preferences[PreferencesKeys.SUBGROUP_LIST] = getSubgroupBundle(subgroupList)
        }
    }

    override suspend fun setRegularityToDataStore(isAuto: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTO_REGULARITY] = isAuto
        }
    }

    override suspend fun setTeacherToDataStore(teacher: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TEACHER] = teacher
        }
    }

    override fun getParamsFromDataStore(): Flow<Quartet<Int, Int, List<String>, Boolean>> =
        dataStore.data.map { preferences ->
            Quartet(
                preferences[PreferencesKeys.COURSE] ?: 1,
                preferences[PreferencesKeys.GROUP] ?: 1,
                preferences[PreferencesKeys.SUBGROUP_LIST]?.let { getSubgroupList(it) }
                    ?: DEFAULT_SUBGROUPS_VALUE,
                preferences[PreferencesKeys.IS_AUTO_REGULARITY] ?: false,
            )
        }

    override fun isListAnimationEnabled(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.IS_LIST_ANIMATION_ENABLED] ?: true
        }

    override suspend fun setListAnimationEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LIST_ANIMATION_ENABLED] = isEnabled
        }
    }

    companion object {
        const val STUDENT_STRING_NAME = "STUDENT"

        object PreferencesKeys {
            val COURSE = intPreferencesKey("course")
            val GROUP = intPreferencesKey("group")
            val SUBGROUP_LIST = stringPreferencesKey("subgroup")
            val IS_AUTO_REGULARITY = booleanPreferencesKey("regularity")
            val ACCOUNT_TYPE = stringPreferencesKey("account_type")
            val IS_SIGNED_IN = booleanPreferencesKey("is_signed_in")
            val TEACHER = stringPreferencesKey("teacher")
            val IS_LIST_ANIMATION_ENABLED = booleanPreferencesKey("is_list_animation_enabled")
        }
    }
}
