package com.kigya.unique.ui.tabs

import androidx.lifecycle.viewModelScope
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.calendar.CalendarWeekdayClickListener
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.data.remote.Resource
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

typealias LessonsResource = Resource<List<Lesson>>

@HiltViewModel
class TabsViewModel @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    appSettings: AppSettings,
    private val repository: LessonRepository,
    logger: Logger
) : BaseViewModel(appSettings, logger, dispatcher), CalendarWeekdayClickListener {

    var selectedDate = CalendarHelper.currentDate
    var previousSelectedDate = CalendarHelper.currentDate

    private var _lessons = MutableStateFlow<LessonsResource>(Resource.Loading())
    val lessons = _lessons.asStateFlow()

    private var course: Int = 1
    private var group: Int = 1
    private var subgroup: String? = null
    private var regularity: String? = null

    init {
        viewModelScope.safeLaunch {
            setInitialState(appSettings)
        }
    }

    private suspend fun setInitialState(appSettings: AppSettings) {
        appSettings.getParamsFromDataStore().collect { params ->
            course = params.first
            group = params.second
            subgroup = params.third
            regularity = params.fourth
            getLessons()
            refreshData()
        }
    }

    private fun refreshData() {
        repository.getLessons(
            course,
            group,
            selectedDate.dayOfWeek.russianValue(),
            subgroup,
            regularity
        )
    }

    private suspend fun getLessons() {
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
        viewModelScope.launch {
            getLessons()
        }
    }
}







