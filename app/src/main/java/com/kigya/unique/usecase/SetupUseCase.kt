package com.kigya.unique.usecase

import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.data.local.settings.AppSettings
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SetupUseCase @Inject constructor(
    private val appSettings: AppSettings,
) {
    fun isUserSignedIn() = appSettings.isSignedIn().take(1)

    suspend fun signIn() {
        appSettings.signIn()
    }

    suspend fun getCurrentAccountType(): AccountType {
        val result = CompletableDeferred<AccountType>()
        appSettings.getCurrentAccountType().take(1).collect {
            result.complete(it)
        }
        return result.await()
    }

    suspend fun setAccountType(accountType: AccountType) {
        appSettings.setCurrentAccountType(accountType)
    }

    suspend fun getIsAuto(): Boolean {
        val result = CompletableDeferred<Boolean>()
        appSettings.getParamsFromDataStore().take(1).collect {
            result.complete(it.fourth)
        }
        return result.await()
    }

    suspend fun isListAnimationEnabled(): Boolean {
        val result = CompletableDeferred<Boolean>()
        appSettings.isListAnimationEnabled().take(1).collect {
            result.complete(it)
        }
        return result.await()
    }

    suspend fun setListAnimationEnabled(isEnabled: Boolean) {
        appSettings.setListAnimationEnabled(isEnabled)
    }
}
