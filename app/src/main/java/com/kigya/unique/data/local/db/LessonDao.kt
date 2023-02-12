package com.kigya.unique.data.local.db

import androidx.room.*
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.constants.ModelConst.DEFAULT_SUBGROUPS_VALUE
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Upsert
    suspend fun upsertLessons(rows: LessonList)

    @Query("SELECT * FROM lessons")
    fun getAllLessons(): Flow<LessonList>

    @Query("DELETE FROM lessons")
    suspend fun deleteAllLessons()

    @Query(
        "SELECT * FROM lessons WHERE `group` = :group AND `course` = :course" +
            " AND (:day IS NULL OR `day` = :day)" +
            " AND (`subgroup` LIKE '' OR `subgroup` IN (:subgroupList))" +
            "AND ((:regularity IS NULL) OR `regularity` LIKE '' OR `regularity` = :regularity)",
    )
    fun getLessons(
        course: Int,
        group: Int,
        day: String? = null,
        subgroupList: List<String> = DEFAULT_SUBGROUPS_VALUE,
        regularity: String? = null,
    ): Flow<List<Lesson>>

    @Query("SELECT COUNT(*) FROM lessons")
    fun getDatabaseSize(): Int
}
