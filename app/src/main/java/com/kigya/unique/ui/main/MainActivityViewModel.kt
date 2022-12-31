package com.kigya.unique.ui.main

import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    appSettings: AppSettings,
    logger: Logger
) : BaseViewModel(appSettings, logger, dispatcher) {

    private var _isUserSignedIn by Delegates.notNull<Boolean>()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }
    }

    val isUserSignedIn: Boolean
        get() = runBlocking {
            appSettings.isSignedIn().take(1).collect {
                _isUserSignedIn = it
            }
            _isUserSignedIn
        }
}