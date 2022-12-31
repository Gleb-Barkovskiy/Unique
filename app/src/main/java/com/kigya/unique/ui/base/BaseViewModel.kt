package com.kigya.unique.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    val appSettings: AppSettings,
    val logger: Logger,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(dispatcher) {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                logError(e)
            }
        }
    }

    private fun logError(e: Throwable) {
        logger.error(javaClass.simpleName, e)
    }

}