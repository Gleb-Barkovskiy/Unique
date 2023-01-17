package com.kigya.unique.data.remote

import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.constants.ModelConst
import com.kigya.unique.utils.extensions.fastReplace
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Document
import java.io.IOException
import javax.inject.Inject

class LessonApi @Inject constructor(
    private val jsoupDocumentApi: JsoupDocumentApi
) : LessonApiSource {

    override suspend fun getNetworkData(): LessonList {
        val rowList = mutableListOf<Lesson>()
        coroutineScope {
            (1..4).map { course ->
                val maxGroup = when (course) {
                    1 -> ModelConst.Groups.MAX_FIRST_COURSE
                    2 -> ModelConst.Groups.MAX_SECOND_COURSE
                    3 -> ModelConst.Groups.MAX_THIRD_COURSE
                    4 -> ModelConst.Groups.MAX_FOURTH_COURSE
                    else -> 0
                }
                (1..maxGroup).map { group ->
                    async() {
                        rowList += getNetworkDataByCourseAndGroup(course, group, false)
                    }
                }
            }
            (1..4).map { course ->
                async() {
                    rowList += getNetworkDataByCourseAndGroup(course, 0, true)
                }
            }
        }
        return rowList
    }

    private suspend fun getNetworkDataByCourseAndGroup(
        course: Int,
        group: Int,
        isMilitaryFaculty: Boolean
    ): LessonList {
        val rowList = mutableListOf<Lesson>()
        val doc: Document? = getDoc(course, group, isMilitaryFaculty)
        val tableList = mutableListOf<String>()
        val subjectTeacherList = mutableListOf<Pair<String, String>>()
        getRows(doc, subjectTeacherList, tableList)
        val result = tableList.chunked(6)
        addResultToList(result, rowList, course, group, subjectTeacherList)
        return rowList
    }


    private fun addResultToList(
        result: List<List<String>>,
        rowList: MutableList<Lesson>,
        course: Int,
        group: Int,
        subjectTeacher: MutableList<Pair<String, String>>
    ) {
        result.forEachIndexed { index, list ->
            rowList.add(
                Lesson(
                    course = course,
                    group = group,
                    day = list[0],
                    time = list[1],
                    subgroup = if (list[2].substringBefore(" ")
                            .contains("н")
                    ) "" else list[2].substringBefore(" "),
                    regularity = list[2].substringAfter(" "),
                    subject = subjectTeacher[index].first,
                    teacher = subjectTeacher[index].second,
                    type = list[4],
                    audience = list[5]
                )
            )
        }
    }

    private fun getRows(
        doc: Document?,
        subjectTeacher: MutableList<Pair<String, String>>,
        tableList: MutableList<String>
    ) {
        doc?.getElementsByTag("tbody")?.forEach { table ->
            table.children()[0].siblingElements().forEach { row ->
                row.children().forEach { cell ->
                    cell.select(".subject-teachers").forEach { child ->
                        val childString = child.toString()
                        val pair = childString.fastReplace("<td class=\"subject-teachers\">", "")
                            .fastReplace("</td>", "").split("<br>")
                        if (pair.size == 2) subjectTeacher.add(Pair(pair[0], pair[1]))
                        else subjectTeacher.add(Pair(pair[0], ""))
                    }
                    tableList.add(cell.text())
                }
            }
        }
    }

    private suspend fun getDoc(course: Int, group: Int, isMilitaryFaculty: Boolean): Document? {
        var document: Document? = null
        try {
            document = if (!isMilitaryFaculty) {
                jsoupDocumentApi.getRegularJsoupDoc(course, group).body()
            } else {
                jsoupDocumentApi.getMilitaryJsoupDoc(course).body()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return document
    }

}