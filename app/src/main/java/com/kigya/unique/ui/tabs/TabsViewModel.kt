package com.kigya.unique.ui.tabs

import androidx.lifecycle.viewModelScope
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.usecase.LoadLessonsUseCase
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.constants.ModelConst.DEFAULT_SUBGROUPS_VALUE
import com.kigya.unique.utils.logger.Logger
import com.kigya.unique.utils.mappers.FiltersMapper.getCourseHintByArrayIndex
import com.kigya.unique.utils.mappers.FiltersMapper.getGroupHintByArrayIndex
import com.kigya.unique.utils.mappers.FiltersMapper.getWeekOptionsStringValue
import com.kigya.unique.utils.wrappers.Resource
import com.kizitonwose.calendar.view.WeekCalendarView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    logger: Logger,
    private val loadLessonsUseCase: LoadLessonsUseCase,
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

    init {
        loadData()
        setWeekMode()
    }

    private fun loadData() {
        viewModelScope.launch(dispatcher) {
            loadLessonsUseCase.loadData(_course, _group, subgroupList, _lessons, selectedDate)
        }
    }

    private fun setWeekMode() {
        viewModelScope.launch(dispatcher) {
            isAutoWeekModeEnabled = loadLessonsUseCase.getIsAuto()
        }
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
            loadLessonsUseCase.setParams(course, group, subgroupList, isAutoWeekModeEnabled)
        }
    }

    private fun swapDates(date: LocalDate) {
        previousSelectedDate = selectedDate
        selectedDate = date
    }

    override fun dateClicked(weekCalendarView: WeekCalendarView, date: LocalDate) {
        enableToScroll()
        swapDates(date)
        weekCalendarView.notifyCalendarChanged()
        viewModelScope.launch(dispatcher) {
            loadLessonsUseCase.loadData(_course, _group, subgroupList, _lessons, selectedDate)
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

    private fun enableToScroll() {
        _shouldScroll.value = true
    }
}
