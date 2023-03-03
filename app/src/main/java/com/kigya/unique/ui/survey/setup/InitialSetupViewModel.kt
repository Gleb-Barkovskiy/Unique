package com.kigya.unique.ui.survey.setup

import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule

@HiltViewModel
class InitialSetupViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val setupUseCase: SetupUseCase,
) : BaseViewModel(dispatcher, logger) {

    private val timer = Timer()

    fun signIn(accountType: AccountType) {
        viewModelScope.launch {
            setupUseCase.setAccountType(accountType)
        }
    }

    fun performAfterDelay(action: () -> Unit) {
        timer.schedule(UI_WAITING_TIME) {
            action()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    companion object {
        const val UI_WAITING_TIME = 1000L
    }
}
