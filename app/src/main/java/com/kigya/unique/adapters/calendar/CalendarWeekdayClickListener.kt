package com.kigya.unique.adapters.calendar

import com.kizitonwose.calendar.view.WeekCalendarView
import java.time.LocalDate

fun interface CalendarWeekdayClickListener {
    fun dateClicked(weekCalendarView: WeekCalendarView, date: LocalDate)
}