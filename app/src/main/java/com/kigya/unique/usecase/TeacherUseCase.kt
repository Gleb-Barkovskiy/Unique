package com.kigya.unique.usecase

import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.data.local.LessonRepository
import com.kigya.unique.data.local.settings.AppSettings
import com.kigya.unique.utils.LessonListResource
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
        return requiredList.filterNotNull().distinct()
    }

    suspend fun getSavedTeacher(): String {
        val result = CompletableDeferred<String>()
        appSettings.getTeacherFromDataStore().take(1).collect {
            result.complete(it)
        }
        return result.await()
    }

    suspend fun saveTeacher(teacher: String) = appSettings.setTeacherToDataStore(teacher)

    suspend fun loadData(
        teacher: String,
        resultLessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
    ) {
        if (repository.getDatabaseSize() == 0) {
            refreshData(teacher, resultLessons, selectedDate)
        } else {
            getDatabaseLessons(teacher, resultLessons, selectedDate)
        }
    }

    private suspend fun refreshData(
        teacher: String,
        resultLessons: MutableStateFlow<LessonListResource>,
        selectedDate: LocalDate,
    ) {
        resultLessons.value = Resource.Loading()
        repository.getLessons().collect { lessons ->
            val filteredLessons = lessons.data?.filter {
                it.teacher == teacher &&
                    it.day == selectedDate.dayOfWeek.russianValue()
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
    ) {
        lessons.value = Resource.Loading()
        repository.getDatabaseLessons(
            teacher,
            selectedDate.dayOfWeek.russianValue(),
        ).collect {
            lessons.value = it
        }
    }
}
