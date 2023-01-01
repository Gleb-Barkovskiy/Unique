package com.kigya.unique.ui.tabs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.adapters.calendar.CalendarDateBinder
import com.kigya.unique.adapters.calendar.CalendarWeekdayBinder
import com.kigya.unique.adapters.calendar.CalendarWeekdayClickListener
import com.kigya.unique.adapters.lesson.LessonAdapter
import com.kigya.unique.adapters.lesson.LessonsScrollListener
import com.kigya.unique.data.dto.lesson.Lesson
import com.kigya.unique.utils.Resource
import com.kigya.unique.databinding.FragmentTabsBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.calendar.CalendarHelper.currentDate
import com.kigya.unique.utils.calendar.CalendarHelper.daysOfWeek
import com.kigya.unique.utils.calendar.CalendarHelper.endDate
import com.kigya.unique.utils.calendar.CalendarHelper.startDate
import com.kigya.unique.utils.converters.LocaleConverter.Russian.russianShortValue
import com.kigya.unique.utils.extensions.observeResource
import com.kigya.unique.utils.extensions.startCenterCircularReveal
import com.kizitonwose.calendar.view.DaySize
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.time.LocalDate

typealias LessonList = List<Lesson>

typealias LessonListResource = Resource<LessonList>

@AndroidEntryPoint
class TabsFragment : BaseFragment(R.layout.fragment_tabs), CalendarDateBinder {

    private val viewBinding by viewBinding(FragmentTabsBinding::bind)
    override val viewModel by viewModels<TabsViewModel>()
    private val adapter by lazy { LessonAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startCenterCircularReveal()
        setupWindow()

        with(viewBinding) {
            setupCalendarView(viewModel, this@TabsFragment)
            setupAdapter()
            observeResource<LessonListResource, LessonList>(viewModel.lessons, resourceView) {
                resourceView.setTryAgainAction { viewModel.refreshData() }
                if (it.isEmpty()) {
                    context?.let { context ->
                        resourceView.setMessageText(context.getString(R.string.no_lessons))
                    }
                    adapter.updateList(emptyList())
                } else {
                    adapter.updateList(it)
                }
            }
        }
    }

    private fun FragmentTabsBinding.observeLessons() {
        observeResource<LessonListResource, LessonList>(viewModel.lessons, resourceView) {
            resourceView.setTryAgainAction { viewModel.refreshData() }
            if (it.isEmpty()) {

                adapter.updateList(emptyList())
            } else {
                adapter.updateList(it)
            }
        }
    }

    private fun FragmentTabsBinding.setupAdapter() {
        rvLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val alphaAdapter = AlphaInAnimationAdapter(
                this@TabsFragment.adapter
            ).apply { setDuration(500) }
            adapter = alphaAdapter
            addOnScrollListener(LessonsScrollListener(this.adapter as AlphaInAnimationAdapter))
        }
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

private fun Fragment.setupWindow() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    activity?.window?.statusBarColor = resources.getColor(R.color.white_base_front, null)
}



