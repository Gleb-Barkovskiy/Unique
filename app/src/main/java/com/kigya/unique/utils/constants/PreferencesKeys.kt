package com.kigya.unique.utils.constants

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val COURSE = intPreferencesKey("course")
    val GROUP = intPreferencesKey("group")
    val SUBGROUP_LIST = stringPreferencesKey("subgroup")
    val IS_AUTO_REGULARITY = booleanPreferencesKey("regularity")
    val ACCOUNT_TYPE = stringPreferencesKey("account_type")
    val IS_SIGNED_IN = booleanPreferencesKey("is_signed_in")
}