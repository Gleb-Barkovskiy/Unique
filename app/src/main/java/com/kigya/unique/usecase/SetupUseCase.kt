package com.kigya.unique.usecase

import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.di.IoDispatcher
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SetupUseCase @Inject constructor(
    private val appSettings: AppSettings,
) {
    fun isUserSignedIn() = appSettings.isSignedIn().take(1)

    suspend fun getCurrentAccountType(): AccountType {
        val result = CompletableDeferred<AccountType>()
        appSettings.getCurrentAccountType().take(1).collect {
            result.complete(it)
        }
        return result.await()
    }

    suspend fun signIn(accountType: AccountType) {
        appSettings.signIn()
        appSettings.setCurrentAccountType(accountType)
    }
}
