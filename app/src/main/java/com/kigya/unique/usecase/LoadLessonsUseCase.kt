package com.kigya.unique.usecase

import android.util.Log
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.mappers.LocaleConverter.Russian.russianValue
import com.kigya.unique.utils.wrappers.Resource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import java.time.LocalDate
import javax.inject.Inject

class LoadLessonsUseCase @Inject constructor(
    private val appSettings: AppSettings,
    private val repository: LessonRepository,
) {
    suspend fun loadData(
        course: MutableStateFlow<Int>,
        group: MutableStateFlow<Int>,
        subgroupList: MutableList<String>,
        lessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
    ) {
        appSettings.getParamsFromDataStore().collect { params ->
            course.value = params.first
            group.value = params.second
            subgroupList.apply {
                clear()
                addAll(params.third)
            }
            val isAuto: Boolean = params.fourth
            Log.d("TabsViewModel", "getIsAutoLoad: $isAuto")
            if (repository.getDatabaseSize() == 0) {
                refreshData(course, group, subgroupList, isAuto, lessons, selectedDate)
            } else {
                getDatabaseLessons(course, group, subgroupList, isAuto, lessons, selectedDate)
            }
        }
    }

    suspend fun getIsAuto(): Boolean {
        val result = CompletableDeferred<Boolean>()
        appSettings.getParamsFromDataStore().take(1).collect {
            Log.d("TabsViewModel", "getIsAuto: $it")
            result.complete(it.fourth)
        }
        return result.await()
    }

    suspend fun setParams(
        course: Int? = null,
        group: Int? = null,
        subgroupList: List<String>? = null,
        isAutoWeekModeEnabled: Boolean? = null,
    ) {
        course?.let { appSettings.setCourseToDataStore(it) }
        group?.let { appSettings.setGroupToDataStore(it) }
        subgroupList?.let { appSettings.setSubgroupListToDataStore(it) }
        isAutoWeekModeEnabled?.let { appSettings.setRegularityToDataStore(it) }
    }

    private suspend fun refreshData(
        course: MutableStateFlow<Int>,
        group: MutableStateFlow<Int>,
        subgroupList: MutableList<String>,
        isAutoWeekModeEnabled: Boolean,
        resultLessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
    ) {
        resultLessons.value = Resource.Loading()
        repository.getLessons().collect { lessons ->
            val filteredLessons = lessons.data?.filter {
                it.course == course.value &&
                    it.group == group.value &&
                    (subgroupList.contains(it.subgroup) || it.subgroup.isNullOrBlank()) &&
                    it.day == selectedDate.dayOfWeek.russianValue()
            }.also { list ->
                if (isAutoWeekModeEnabled) {
                    list?.filter {
                        (
                            it.regularity == CalendarHelper.getWeekStringValueAbbreviation(
                                selectedDate,
                            ) || it.regularity.isNullOrBlank()
                            )
                    }
                }
            }
            resultLessons.value =
                filteredLessons?.let { Resource.Success(it) }
                    ?: Resource.Error(Exception("No lessons"))
        }
    }

    private suspend fun getDatabaseLessons(
        course: MutableStateFlow<Int>,
        group: MutableStateFlow<Int>,
        subgroupList: MutableList<String>,
        isAutoWeekModeEnabled: Boolean,
        lessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
    ) {
        lessons.value = Resource.Loading()
        Log.d("TabsViewModel", "getDatabaseLessons: $isAutoWeekModeEnabled")
        repository.getDatabaseLessons(
            course.value,
            group.value,
            selectedDate.dayOfWeek.russianValue(),
            subgroupList,
            if (isAutoWeekModeEnabled) {
                CalendarHelper.getWeekStringValueAbbreviation(selectedDate)
            } else {
                null
            },
        ).collect {
            lessons.value = it
        }
    }
}
