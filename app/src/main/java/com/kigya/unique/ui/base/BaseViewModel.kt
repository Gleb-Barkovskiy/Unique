package com.kigya.unique.ui.base

import androidx.lifecycle.ViewModel
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    val logger: Logger,
) : ViewModel()
