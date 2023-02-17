package com.kigya.unique.ui.tabs.sheet.teacher

import androidx.lifecycle.viewModelScope
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.usecase.TeacherUseCase
import com.kigya.unique.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class DialogTeacherViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val setupUseCase: SetupUseCase,
    private val teacherUseCase: TeacherUseCase,
) : BaseViewModel(dispatcher, logger) {

    var teacherList by Delegates.notNull<List<String>>()
    var currentTeacher by Delegates.notNull<String>()

    init {
        currentTeacher = getSavedTeacher()

        teacherList = getTeachers()

    }

    private fun getTeachers(): List<String> = runBlocking { teacherUseCase.getTeacherList() }

    private fun getSavedTeacher() = runBlocking { teacherUseCase.getSavedTeacher() }

    fun saveTeacher(teacher: String) =
        viewModelScope.launch(dispatcher) {
            teacherUseCase.saveTeacher(teacher)
        }
}
