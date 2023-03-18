package com.kigya.unique.ui.settings

import androidx.lifecycle.viewModelScope
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val setupUseCase: SetupUseCase,
) : BaseViewModel(dispatcher, logger) {

    fun isAnimationEnabled() = runBlocking { setupUseCase.isListAnimationEnabled() }

    fun setListAnimationEnabled(value: Boolean) {
        viewModelScope.launch {
            setupUseCase.setListAnimationEnabled(value)
        }
    }
}
