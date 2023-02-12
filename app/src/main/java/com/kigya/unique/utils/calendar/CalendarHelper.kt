package com.kigya.unique.utils.calendar

import android.view.ViewGroup
import android.widget.TextView
import com.kigya.unique.App
import com.kigya.unique.R
import com.kigya.unique.utils.calendar.CalendarHelper.Const.FEBRUARY_MONTH
import com.kigya.unique.utils.calendar.CalendarHelper.Const.FEBRUARY_SECOND_WEEK_GUARANTEED
import com.kigya.unique.utils.calendar.CalendarHelper.Const.SEPTEMBER_FIRST_WEEK_GUARANTEED
import com.kigya.unique.utils.calendar.CalendarHelper.Const.SEPTERMBER_MONTH
import com.kigya.unique.utils.calendar.CalendarHelper.Const.WEEK_SERIAL_DELIMITER
import com.kigya.unique.utils.constants.ModelConst.FIRST_WEEK_STRING_SHORT
import com.kigya.unique.utils.constants.ModelConst.SECOND_WEEK_STRING_SHORT
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import kotlin.math.abs

object CalendarHelper {
    private val currentMonth: YearMonth = YearMonth.now()
    val currentDate: LocalDate = LocalDate.now()
    val startDate: LocalDate = currentMonth.atStartOfMonth()
    val endDate: LocalDate = currentMonth.atEndOfMonth()
    val daysOfWeek = daysOfWeek().filter { it != DayOfWeek.SUNDAY }

    fun setCalendarActiveViewParams(
        tvDate: TextView,
        tvWeekDay: TextView,
        wrapper: ViewGroup,
    ) {
        with(App.appContext) {
            tvDate.setTextColor(getColor(R.color.white_base_front))
            tvWeekDay.setTextColor(getColor(R.color.white_base_dark))
            wrapper.backgroundTintList = getColorStateList(R.color.green_base)
        }
    }

    fun setCalendarInactiveCalendarParams(
        tvDate: TextView,
        tvWeekDay: TextView,
        wrapper: ViewGroup,
    ) {
        with(App.appContext) {
            tvDate.setTextColor(getColor(R.color.black_base))
            tvWeekDay.setTextColor(getColor(R.color.white_base_dark))
            wrapper.backgroundTintList = getColorStateList(R.color.white_base_front)
        }
    }

    private fun getWeekSerialNumber(date: LocalDate): Int =
        date.get(WeekFields.ISO.weekOfWeekBasedYear())

    fun getWeekStringValueAbbreviation(date: LocalDate): String =
        if (isFirstWeek(date)) FIRST_WEEK_STRING_SHORT else SECOND_WEEK_STRING_SHORT

    private fun isFirstWeek(date: LocalDate): Boolean {
        val currentWeekNumber = getWeekSerialNumber(date)
        val isFirstSemester = currentWeekNumber > WEEK_SERIAL_DELIMITER

        val startWeekCount = if (isFirstSemester) {
            getWeekSerialNumber(
                currentDate.withMonth(SEPTERMBER_MONTH).withDayOfMonth(
                    SEPTEMBER_FIRST_WEEK_GUARANTEED,
                ),
            )
        } else {
            getWeekSerialNumber(
                currentDate.withMonth(FEBRUARY_MONTH)
                    .withDayOfMonth(FEBRUARY_SECOND_WEEK_GUARANTEED),
            )
        }

        return abs(currentWeekNumber - startWeekCount) % 2 == 0
    }

    object Const {
        const val WEEK_SERIAL_DELIMITER = 25
        const val SEPTERMBER_MONTH = 9
        const val FEBRUARY_MONTH = 2
        const val SEPTEMBER_FIRST_WEEK_GUARANTEED = 1
        const val FEBRUARY_SECOND_WEEK_GUARANTEED = 8
    }
}
