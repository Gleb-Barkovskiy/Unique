package com.kigya.unique.adapters.calendar

import android.view.View
import com.kigya.unique.adapters.calendar.interlayers.CalendarDateBinder
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder

class CalendarWeekdayBinder(
    private val calendarView: WeekCalendarView,
    private val clickListener: CalendarWeekdayClickListener,
    private val dateBinder: CalendarDateBinder
) : WeekDayBinder<CalendarWeekdayViewContainer> {
    override fun create(view: View): CalendarWeekdayViewContainer =
        CalendarWeekdayViewContainer(view, calendarView, clickListener)

    override fun bind(container: CalendarWeekdayViewContainer, data: WeekDay) {
        with(container) {
            weekDay = data
            dateBinder.bindDate(data.date, tvDay, tvWeekDay, wrapper)
        }
    }
}
