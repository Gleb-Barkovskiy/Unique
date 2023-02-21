package com.kigya.unique.adapters.calendar

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.kigya.unique.App
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kigya.unique.databinding.CalendarDayItemBinding
import com.kigya.unique.utils.extensions.ui.view.setOnSafeClickListener
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import kotlin.properties.Delegates

class CalendarWeekdayViewContainer(
    view: View,
    calendarView: WeekCalendarView,
    clickListener: CalendarWeekdayClickListener,
) : ViewContainer(view) {

    var tvDay by Delegates.notNull<TextView>()
    var tvWeekDay by Delegates.notNull<TextView>()
    var wrapper by Delegates.notNull<CardView>()

    var weekDay by Delegates.notNull<WeekDay>()

    init {
        with(CalendarDayItemBinding.bind(view)) { fillContainer() }
        view.setOnSafeClickListener { clickListener.dateClicked(calendarView, weekDay.date) }
    }

    private fun CalendarDayItemBinding.fillContainer() {
        tvDay = tvDayItemDate
        tvWeekDay = tvDayItemWeekDay
        wrapper = cvDayItemCalendar
    }
}
