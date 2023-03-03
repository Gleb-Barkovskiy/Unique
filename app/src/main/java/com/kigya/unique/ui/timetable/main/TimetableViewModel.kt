package com.kigya.unique.ui.timetable.main

import androidx.lifecycle.viewModelScope
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kigya.unique.data.dto.account.AccountType
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.SetupUseCase
import com.kigya.unique.usecase.StudentUseCase
import com.kigya.unique.usecase.TeacherUseCase
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.constants.ModelConst.DEFAULT_SUBGROUPS_VALUE
import com.kigya.unique.utils.helpers.CalendarHelper
import com.kigya.unique.utils.logger.Logger
import com.kigya.unique.utils.mappers.AccountTypeMapper
import com.kigya.unique.utils.mappers.FiltersMapper.getCourseHintByArrayIndex
import com.kigya.unique.utils.mappers.FiltersMapper.getGroupHintByArrayIndex
import com.kigya.unique.utils.mappers.FiltersMapper.getWeekOptionsStringValue
import com.kigya.unique.utils.wrappers.Resource
import com.kizitonwose.calendar.view.WeekCalendarView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class TimetableViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val setupUseCase: SetupUseCase,
    private val studentUseCase: StudentUseCase,
    private val teacherUseCase: TeacherUseCase,
) : BaseViewModel(dispatcher, logger), CalendarWeekdayClickListener {

    var selectedDate = CalendarHelper.currentDate
    var previousSelectedDate = CalendarHelper.currentDate

    private var _shouldScroll = MutableStateFlow(false)
    val shouldScroll = _shouldScroll.asStateFlow()

    private var _lessons = MutableStateFlow<LessonListResource>(Resource.Loading())
    val lessons = _lessons.asStateFlow()

    private val _course: MutableStateFlow<Int> = MutableStateFlow(1)
    val course = _course.asStateFlow().value

    private var _group: MutableStateFlow<Int> = MutableStateFlow(1)
    val group = _group.asStateFlow().value

    val subgroupList: MutableList<String> = DEFAULT_SUBGROUPS_VALUE.toMutableList()

    private var isAutoWeekModeEnabled: Boolean = true

    val savedTeacher by lazy { runBlocking { teacherUseCase.getSavedTeacher() } }
    var teacherList by Delegates.notNull<List<String>>()

    init {
        loadData()
        setWeekMode()
        teacherList = getTeachers()
    }

    fun isStudentMode(): Boolean =
        runBlocking { setupUseCase.getCurrentAccountType() == AccountType.STUDENT }

    private fun getTeachers(): List<String> = runBlocking { teacherUseCase.getTeacherList() }

    fun saveParams(teacher: String, isAuto: Boolean?) =
        viewModelScope.launch(dispatcher) {
            teacherUseCase.setParams(teacher, isAuto)
        }

    override fun dateClicked(weekCalendarView: WeekCalendarView, date: LocalDate) {
        enableToScroll()
        swapDates(date)
        weekCalendarView.notifyCalendarChanged()
        if (!isStudentMode()) {
            viewModelScope.launch(dispatcher) {
                teacherUseCase.loadData(savedTeacher, _lessons, selectedDate, isAutoWeekModeEnabled)
            }
        } else {
            viewModelScope.launch(dispatcher) {
                studentUseCase.loadData(_course, _group, subgroupList, _lessons, selectedDate)
            }
        }
    }

    fun toNextDay(weekCalendarView: WeekCalendarView) {
        if (selectedDate >= CalendarHelper.endDate && selectedDate.dayOfWeek == DayOfWeek.SUNDAY) return
        dateClicked(weekCalendarView, selectedDate.plusDays(1))
        weekCalendarView.scrollToDate(selectedDate)
    }

    fun toPreviousDay(weekCalendarView: WeekCalendarView) {
        if (selectedDate <= CalendarHelper.startDate && selectedDate.dayOfWeek == DayOfWeek.MONDAY) return
        dateClicked(weekCalendarView, selectedDate.minusDays(1))
        weekCalendarView.scrollToDate(selectedDate)
    }

    fun getCourseHint() = getCourseHintByArrayIndex(_course.value)

    fun getGroupHint() = getGroupHintByArrayIndex(_group.value, _course.value)

    fun getWeekModeHint() = getWeekOptionsStringValue(isAutoWeekModeEnabled)

    fun setParams(
        course: Int? = null,
        group: Int? = null,
        subgroupList: List<String>? = null,
        isAutoWeekModeEnabled: Boolean? = null,
    ) {
        viewModelScope.launch(dispatcher) {
            studentUseCase.setParams(course, group, subgroupList, isAutoWeekModeEnabled)
        }
    }

    fun setAccountType(index: Int) {
        runBlocking {
            setupUseCase.setAccountType(AccountTypeMapper.mapSelectionToAccountType(index))
        }
    }

    private fun loadData() {
        viewModelScope.launch(dispatcher) {
            if (setupUseCase.getCurrentAccountType() == AccountType.TEACHER) {
                viewModelScope.launch(dispatcher) {
                    teacherUseCase.loadData(
                        teacherUseCase.getSavedTeacher(),
                        _lessons,
                        selectedDate,
                        isAutoWeekModeEnabled,
                    )
                }
            } else {
                studentUseCase.loadData(_course, _group, subgroupList, _lessons, selectedDate)
            }
        }
    }

    fun isUserSignedIn(): Boolean =
        runBlocking {
            val result = CompletableDeferred<Boolean>()
            setupUseCase.isUserSignedIn().collect {
                result.complete(it)
            }
            return@runBlocking result.await()
        }

    fun signIn() {
        viewModelScope.launch(dispatcher) {
            setupUseCase.signIn()
        }
    }

    private fun setWeekMode() {
        viewModelScope.launch(dispatcher) {
            isAutoWeekModeEnabled = setupUseCase.getIsAuto()
        }
    }

    private fun swapDates(date: LocalDate) {
        previousSelectedDate = selectedDate
        selectedDate = date
    }

    private fun enableToScroll() {
        _shouldScroll.value = true
    }
}
