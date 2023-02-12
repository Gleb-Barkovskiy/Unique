package com.kigya.unique.ui.survey.onboarding

import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.ui.survey.onboarding.OnboardingFragment.Companion.OnboardingConst
import com.kigya.unique.ui.survey.onboarding.OnboardingViewModel.OnboardingUiState.GesturePending
import com.kigya.unique.ui.survey.onboarding.OnboardingViewModel.OnboardingUiState.GestureReceived
import com.kigya.unique.utils.extensions.ui.flow.collectFlow
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule

typealias ViewStateFlow = MutableStateFlow<OnboardingViewModel.ViewState>

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val repository: LessonRepository,
) : BaseViewModel(dispatcher, logger) {

    private val _retainer: ViewStateFlow = MutableStateFlow(ViewState(GesturePending))
    val retainer = _retainer.asStateFlow()
    var isReady = false

    private val timer = Timer()

    init {
        prepareDatabaseFromAssets(dispatcher)
        loadFreshData()
    }

    private fun loadFreshData() {
        viewModelScope.launch {
            repository.getLessons().collect {}
        }
    }

    private fun prepareDatabaseFromAssets(dispatcher: CoroutineDispatcher) {
        viewModelScope.launch(dispatcher) {
            repository.setToDatabaseFromAssets()
        }
    }

    fun performAfterDelay(action: () -> Unit) {
        timer.schedule(OnboardingConst.UI_WAITING_TIME) {
            if (_retainer.value.uiState == GesturePending) {
                handleIfPending {
                    changeReceive()
                    action()
                }
            }
        }
    }

    fun onUiTriggered(action: () -> Unit) {
        changeReceive()
        action()
    }

    fun handleIfPending(action: () -> Unit) {
        collectFlow(_retainer) {
            if (it.uiState == GesturePending) {
                action()
            }
        }
    }

    fun handleIfTriggered(
        viewState: ViewState,
        gestureHandled: () -> Unit,
    ) {
        if (viewState.uiState == GestureReceived) gestureHandled()
    }

    private fun changeReceive() {
        _retainer.value = ViewState(GestureReceived)
        isReady = true
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    data class ViewState(
        val uiState: OnboardingUiState,
    )

    sealed class OnboardingUiState {
        object GesturePending : OnboardingUiState()
        object GestureReceived : OnboardingUiState()
    }
}
