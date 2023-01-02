package com.kigya.unique.utils.calendar

import android.view.ViewGroup
import android.widget.TextView
import com.kigya.unique.App
import com.kigya.unique.R
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth

object CalendarHelper {
    val currentDate: LocalDate = LocalDate.now()
    private val currentMonth: YearMonth = YearMonth.now()
    val startDate: LocalDate = currentMonth.minusMonths(0).atStartOfMonth()
    val endDate: LocalDate = currentMonth.plusMonths(0).atEndOfMonth()
    val daysOfWeek = daysOfWeek().filter { it != java.time.DayOfWeek.SUNDAY }

    fun setActive(
        tvDate: TextView,
        tvWeekDay: TextView,
        wrapper: ViewGroup
    ) {
        tvDate.setTextColor(App.appContext.getColor(Color.WHITE_BASE_FRONT))
        tvWeekDay.setTextColor(App.appContext.getColor(Color.WHITE_BASE_DARK))
        wrapper.backgroundTintList =
            App.appContext.getColorStateList(Color.GREEN_BASE)
    }

    fun setInactive(
        tvDate: TextView,
        tvWeekDay: TextView,
        wrapper: ViewGroup
    ) {
        tvDate.setTextColor(App.appContext.getColor(Color.BLACK_BASE))
        tvWeekDay.setTextColor(App.appContext.getColor(Color.WHITE_BASE_DARK))
        wrapper.backgroundTintList =
            App.appContext.getColorStateList(Color.WHITE_BASE_FRONT)
    }


    object Color {
        @JvmStatic val BLACK_BASE = R.color.black_base
        @JvmStatic val WHITE_BASE_FRONT = R.color.white_base_front
        @JvmStatic val WHITE_BASE_DARK = R.color.white_base_dark
        @JvmStatic val GREEN_BASE = R.color.green_base
    }

}