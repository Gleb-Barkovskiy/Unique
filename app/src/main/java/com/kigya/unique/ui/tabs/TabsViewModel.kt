package com.kigya.unique.ui.tabs

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.adapters.calendar.CalendarWeekdayClickListener
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.utils.Resource
import com.kigya.unique.di.IoDispatcher
import com.kigya.unique.ui.base.BaseViewModel
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.converters.LocaleConverter.Russian.russianValue
import com.kigya.unique.utils.logger.Logger
import com.kizitonwose.calendar.view.WeekCalendarView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
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

    private var _lessons = MutableStateFlow<LessonListResource>(Resource.Loading())
    val lessons = _lessons.asStateFlow()

    private var course: Int = 1
    private var group: Int = 1
    private var subgroup: String? = null
    private var regularity: String? = null

    init {
        setParamsStoreState(appSettings)
        initLessonsFromDatabaseOrRefresh()
        loadFreshData()
    }

    private fun loadFreshData() {
        viewModelScope.launch {
            repository.getLessons().collect {}
        }
    }

    private fun initLessonsFromDatabaseOrRefresh() {
        viewModelScope.launch(dispatcher) {
            getFromDatabaseOrRefresh()
        }
    }

    private fun setParamsStoreState(appSettings: AppSettings) {
        viewModelScope.launch(dispatcher) {
            Log.d("LessonRepository", "setParamsStoreState")
            appSettings.getParamsFromDataStore().collect { params ->
                course = params.first
                group = params.second
                subgroup = params.third
                regularity = params.fourth
            }
        }
    }

    fun refreshData() {
        Log.d("Observer", "refreshData")
        viewModelScope.launch(dispatcher) {
            _lessons.value = Resource.Loading()
            repository.getLessons().collect { lessons ->
                val filteredLessons = lessons.data?.filter {
                    it.course == course &&
                            it.group == group &&
                            it.subgroup == subgroup &&
                            it.regularity == regularity &&
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
        Log.d("LessonRepository", "getDatabaseLessons")
        repository.getDatabaseLessons(
            course,
            group,
            selectedDate.dayOfWeek.russianValue(),
            subgroup,
            regularity
        ).collect {
            _lessons.value = it
        }
    }

    private fun swapDates(date: LocalDate) {
        previousSelectedDate = selectedDate
        selectedDate = date
    }

    override fun dateClicked(weekCalendarView: WeekCalendarView, date: LocalDate) {
        swapDates(date)
        weekCalendarView.notifyCalendarChanged()
        viewModelScope.launch(dispatcher) {
            Log.d("LessonRepository", "dateClicked")
            getFromDatabaseOrRefresh()
        }
    }

    private suspend fun getFromDatabaseOrRefresh() {
        Log.d("LessonRepository", "getFromDatabaseOrRefresh")
        if (repository.getDatabaseSize() == 0) {
            Log.d("LessonRepository", "getFromDatabaseOrRefresh: refresh")
            refreshData()
        } else {
            Log.d("LessonRepository", "getFromDatabaseOrRefresh: getDatabaseLessons")
            getDatabaseLessons()
        }
    }
}