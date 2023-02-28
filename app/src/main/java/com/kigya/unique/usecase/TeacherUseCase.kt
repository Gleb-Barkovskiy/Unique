package com.kigya.unique.usecase

import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.helpers.CalendarHelper
import com.kigya.unique.utils.mappers.FiltersMapper
import com.kigya.unique.utils.mappers.LocaleConverter.Russian.russianValue
import com.kigya.unique.utils.wrappers.Resource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import java.time.LocalDate
import javax.inject.Inject

class TeacherUseCase @Inject constructor(
    private val appSettings: AppSettings,
    private val repository: LessonRepository,
) {
    suspend fun getTeacherList(): List<String> {
        val result = CompletableDeferred<List<Lesson>>()
        repository.getAllLessons().take(1).collect {
            it.data?.let { it1 -> result.complete(it1) }
        }
        val requiredList = mutableListOf<String?>()
        result.await().forEach { lesson ->
            val teacherString = lesson.teacher
            if (teacherString.split(", ").size > 1) {
                teacherString.split(", ").forEach {
                    requiredList.add(FiltersMapper.getTeacherOrNull(it))
                }
            } else {
                requiredList.add(FiltersMapper.getTeacherOrNull(teacherString))
            }
        }
        return FiltersMapper.getDistinctTeachersWithFormatting(
            requiredList.filterNotNull().distinct(),
        )
    }

    suspend fun getSavedTeacher(): String {
        val result = CompletableDeferred<String>()
        appSettings.getTeacherFromDataStore().take(1).collect {
            result.complete(it)
        }
        val teacher = result.await()
        return teacher.ifEmpty {
            val firstTeacherInList = getTeacherList().first()
            saveTeacher(firstTeacherInList)
            firstTeacherInList
        }
    }

    private suspend fun saveTeacher(teacher: String) =
        appSettings.setTeacherToDataStore(teacher)

    suspend fun setParams(
        teacher: String,
        isAutoWeekModeEnabled: Boolean? = null,
    ) {
        appSettings.setTeacherToDataStore(teacher)
        isAutoWeekModeEnabled?.let { appSettings.setRegularityToDataStore(it) }
    }

    suspend fun loadData(
        teacher: String,
        resultLessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
        isAutoWeekModeEnabled: Boolean,
    ) {
        if (repository.getDatabaseSize() == 0) {
            refreshData(teacher, resultLessons, selectedDate, isAutoWeekModeEnabled)
        } else {
            getDatabaseLessons(teacher, resultLessons, selectedDate, isAutoWeekModeEnabled)
        }
    }

    private suspend fun refreshData(
        teacher: String,
        resultLessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
        isAutoWeekModeEnabled: Boolean,
    ) {
        resultLessons.value = Resource.Loading()
        repository.getLessons().collect { lessons ->
            val filteredLessons = lessons.data?.filter {
                it.teacher == teacher &&
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
        teacher: String,
        lessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
        isAutoWeekModeEnabled: Boolean,
    ) {
        lessons.value = Resource.Loading()
        repository.getDatabaseLessons(
            teacher,
            selectedDate.dayOfWeek.russianValue(),
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
