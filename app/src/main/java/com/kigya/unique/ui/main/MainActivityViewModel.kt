package com.kigya.unique.ui.main

import androidx.lifecycle.viewModelScope
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.ScheduleWorkUseCase
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    logger: Logger,
    scheduleWorkUseCase: ScheduleWorkUseCase,
    private val setupUseCase: SetupUseCase,
) : BaseViewModel(dispatcher, logger) {

    private var _isUserSignedIn by Delegates.notNull<Boolean>()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(SPLASH_DELAY)
            _isLoading.value = false
        }
        scheduleWorkUseCase()
    }

    val isUserSignedIn: Boolean
        get() = runBlocking {
            setupUseCase.isUserSignedIn().collect {
                _isUserSignedIn = it
            }
            _isUserSignedIn
        }

    companion object {
        private const val SPLASH_DELAY = 2000L
    }
}
