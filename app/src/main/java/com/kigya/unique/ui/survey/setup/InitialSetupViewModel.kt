package com.kigya.unique.ui.survey.setup

import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.utils.constants.InitialSetupConst
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class InitialSetupViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    appSettings: AppSettings,
    logger: Logger
) : BaseViewModel(appSettings, logger, dispatcher) {

    private val timer = Timer()

    fun signIn(accountType: AccountType) {
        viewModelScope.launch {
            appSettings.signIn()
            appSettings.setCurrentAccountType(accountType)
        }
    }

    fun performAfterDelay(action: () -> Unit) {
        timer.schedule(InitialSetupConst.UI_WAITING_TIME) {
            action()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}