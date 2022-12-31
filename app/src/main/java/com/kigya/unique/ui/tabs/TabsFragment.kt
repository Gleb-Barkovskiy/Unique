package com.kigya.unique.ui.tabs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.data.local.calendar.CalendarWeekdayBinder
import com.kigya.unique.data.local.calendar.CalendarWeekdayClickListener
import com.kigya.unique.data.local.calendar.CalendarDateBinder
import com.kigya.unique.data.remote.Resource
import com.kigya.unique.databinding.FragmentTabsBinding
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.calendar.CalendarHelper.currentDate
import com.kigya.unique.utils.calendar.CalendarHelper.daysOfWeek
import com.kigya.unique.utils.calendar.CalendarHelper.endDate
import com.kigya.unique.utils.calendar.CalendarHelper.startDate
import com.kigya.unique.utils.converters.LocaleConverter.Russian.russianShortValue
import com.kigya.unique.utils.extensions.startCenterCircularReveal
import com.kigya.unique.utils.logger.LogCatLogger
import com.kizitonwose.calendar.view.DaySize
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs), CalendarDateBinder {

    private val viewBinding by viewBinding(FragmentTabsBinding::bind)
    private val viewModel by viewModels<TabsViewModel>()
    private val adapter by lazy { LessonAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startCenterCircularReveal()
        setWindowLimits()

        with(viewBinding) {
            setupCalendarView(viewModel, this@TabsFragment)
            setupAdapter()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.lessons.collect {
                    when (it) {
                        is Resource.Success -> {
                            adapter.lessons = it.data ?: emptyList()
                            adapter.notifyDataSetChanged()
                        }

                        else -> {
                            LogCatLogger.log("TabsFragment", "onViewCreated: $it")
                        }
                    }
                }
            }
        }
    }

    private fun FragmentTabsBinding.setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        rvLessons.layoutManager = layoutManager
        rvLessons.adapter = adapter
    }

    override fun bindDate(
        date: LocalDate,
        tvDate: TextView,
        tvWeekDay: TextView,
        wrapper: ViewGroup
    ) {
        tvDate.text = date.dayOfMonth.toString()
        tvWeekDay.text = date.dayOfWeek.russianShortValue()
        if (date == viewModel.previousSelectedDate) {
            CalendarHelper.setInactive(tvDate, tvWeekDay, wrapper)
        }
        if (date == viewModel.selectedDate) {
            CalendarHelper.setActive(tvDate, tvWeekDay, wrapper)
        } else {
            CalendarHelper.setInactive(tvDate, tvWeekDay, wrapper)
        }
    }
}

private fun FragmentTabsBinding.setupCalendarView(
    listener: CalendarWeekdayClickListener,
    dateBinder: CalendarDateBinder
) {
    calendarView.apply {
        dayBinder = CalendarWeekdayBinder(calendarView, listener, dateBinder)
        setup(
            startDate,
            endDate,
            daysOfWeek.first()
        )
        daySize = DaySize.SeventhWidth
        scrollToDate(currentDate)
    }
}

private fun Fragment.setWindowLimits() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}



