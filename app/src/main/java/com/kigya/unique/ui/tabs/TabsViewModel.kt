package com.kigya.unique.ui.tabs

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.utils.wrappers.Resource
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.constants.ModelConst.DEFAULT_SUBGROUPS_VALUE
import com.kigya.unique.utils.mappers.LocaleConverter.Russian.russianValue
import com.kigya.unique.utils.logger.Logger
import com.kigya.unique.utils.mappers.FiltersMapper.toCourseHint
import com.kigya.unique.utils.mappers.FiltersMapper.toGroupHint
import com.kigya.unique.utils.mappers.FiltersMapper.toWeekHint
import com.kizitonwose.calendar.view.WeekCalendarView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.function.LongFunction
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    appSettings: AppSettings,
    private val repository: LessonRepository,
    logger: Logger
) : BaseViewModel(appSettings, logger, dispatcher), CalendarWeekdayClickListener {

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

    private var _subgroupList: MutableStateFlow<MutableList<String>> =
        MutableStateFlow(DEFAULT_SUBGROUPS_VALUE.toMutableList())
    val subgroupList = _subgroupList.asStateFlow().value

    private var _isAutoWeekModeEnabled = MutableStateFlow(true)
    val isAutoWeekModeEnabled = _isAutoWeekModeEnabled.asStateFlow().value

    init {
        loadData(appSettings)
    }

    private fun loadData(appSettings: AppSettings) {
        viewModelScope.launch(dispatcher) {
            appSettings.getParamsFromDataStore().collect { params ->
                _course.value = params.first
                _group.value = params.second
                _subgroupList.value = params.third.toMutableList()
                _isAutoWeekModeEnabled.value = params.fourth
                getFromDatabaseOrRefresh()
            }
        }
    }

    fun getCourseHint() = _course.value.toCourseHint()

    fun getGroupHint() = _group.value.toGroupHint(_course.value)

    fun getWeekModeHint() = _isAutoWeekModeEnabled.value.toWeekHint()

    fun unableToScroll() {
        _shouldScroll.value = false
    }


    private fun loadFreshData() {
        viewModelScope.launch() {
            repository.getLessons().collect {}
        }
    }

    fun setParams(
        course: Int? = null,
        group: Int? = null,
        subgroupList: List<String>? = null,
        isAutoWeekModeEnabled: Boolean? = null
    ) {
        viewModelScope.launch(dispatcher) {
            course?.let { appSettings.setCourseToDataStore(it) }
            group?.let { appSettings.setGroupToDataStore(it) }
            subgroupList?.let { appSettings.setSubgroupListToDataStore(it) }
            isAutoWeekModeEnabled?.let { appSettings.setRegularityToDataStore(it) }
            loadData(appSettings)
        }
    }


    fun refreshData() {
        viewModelScope.launch(dispatcher) {
            _lessons.value = Resource.Loading()
            repository.getLessons().collect { lessons ->
                val filteredLessons = lessons.data?.filter {
                    it.course == _course.value &&
                            it.group == _group.value &&
                            subgroupList.contains(it.subgroup)
                    // regularity
                    it.day == selectedDate.dayOfWeek.russianValue()
                }
                _lessons.value =
                    filteredLessons?.let { Resource.Success(it) }
                        ?: Resource.Error(Exception("No lessons"))
            }
        }
    }

    private suspend fun getDatabaseLessons() {
        _lessons.value = Resource.Loading()
        repository.getDatabaseLessons(
            _course.value,
            _group.value,
            selectedDate.dayOfWeek.russianValue(),
            _subgroupList.value.toList(),
            null
        ).collect {
            _lessons.value = it
            lessons.value.data?.let { lessons ->
                if (lessons.isEmpty()) {
                    refreshData()
                }
            }
        }
    }

    private fun swapDates(date: LocalDate) {
        previousSelectedDate = selectedDate
        selectedDate = date
    }

    override fun dateClicked(weekCalendarView: WeekCalendarView, date: LocalDate) {
        if (date == selectedDate || date.dayOfWeek == DayOfWeek.SUNDAY) return
        enableToScroll()
        swapDates(date)
        weekCalendarView.notifyCalendarChanged()
        viewModelScope.launch(dispatcher) {
            getFromDatabaseOrRefresh()
        }
    }

    private fun enableToScroll() {
        _shouldScroll.value = true
    }

    private suspend fun getFromDatabaseOrRefresh() {
        if (repository.getDatabaseSize() == 0) {
            refreshData()
        } else {
            getDatabaseLessons()
        }
    }
}