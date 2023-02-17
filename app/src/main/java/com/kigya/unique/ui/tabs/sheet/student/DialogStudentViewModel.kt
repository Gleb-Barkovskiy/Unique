package com.kigya.unique.ui.tabs.sheet.student

import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.usecase.StudentUseCase
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class DialogStudentViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val setupUseCase: SetupUseCase,
    private val timetableUseCase: StudentUseCase,
) : BaseViewModel(dispatcher, logger)
