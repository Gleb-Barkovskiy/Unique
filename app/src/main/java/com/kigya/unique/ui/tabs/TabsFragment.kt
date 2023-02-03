package com.kigya.unique.ui.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.adapters.calendar.CalendarWeekdayBinder
import com.kigya.unique.adapters.calendar.interlayers.CalendarDateBinder
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kigya.unique.adapters.lesson.LessonAdapter
import com.kigya.unique.adapters.lesson.LessonSmoothScroller
import com.kigya.unique.adapters.lesson.LessonsScrollListener
import com.kigya.unique.databinding.FragmentTabsBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.calendar.CalendarHelper
import com.kigya.unique.utils.calendar.CalendarHelper.currentDate
import com.kigya.unique.utils.calendar.CalendarHelper.daysOfWeek
import com.kigya.unique.utils.calendar.CalendarHelper.endDate
import com.kigya.unique.utils.calendar.CalendarHelper.startDate
import com.kigya.unique.utils.extensions.ui.collectFlow
import com.kigya.unique.utils.extensions.ui.observeResource
import com.kigya.unique.utils.extensions.ui.view.startCenterCircularReveal
import com.kigya.unique.utils.mappers.LocaleConverter.Russian.russianShortValue
import com.kizitonwose.calendar.view.DaySize
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.time.LocalDate

@AndroidEntryPoint
class TabsFragment : BaseFragment(R.layout.fragment_tabs), CalendarDateBinder {

    private val viewBinding by viewBinding(FragmentTabsBinding::bind)
    override val viewModel by viewModels<TabsViewModel>()
    private val adapter by lazy { LessonAdapter(this.requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startCenterCircularReveal()
        setupWindow()

        with(viewBinding) {
            setupCalendarView(viewModel, this@TabsFragment)
            setupRecycler()
            observeLessons()
            observeScroll()
            addShowOptionsClickListener()
        }
    }

    private fun FragmentTabsBinding.addShowOptionsClickListener() {
        btnOptions.setOnClickListener {
            val bottomFragment = BottomFragment.newInstance()
            with(viewModel) {
                bottomFragment.arguments = Bundle().apply {
                    putString(ARG_COURSE, getCourseHint())
                    putString(ARG_GROUP, getGroupHint())
                    putString(ARG_WEEK, getWeekModeHint())
                    putStringArrayList(ARG_SUBGROUPS, ArrayList(subgroupList))
                }
            }
            bottomFragment.show(parentFragmentManager, BOTTOM_SHEET_TAG)
        }
    }

    private fun FragmentTabsBinding.observeScroll() {
        with(viewModel) {
            collectFlow(viewModel.shouldScroll) { shouldScroll ->
                if (shouldScroll) {
                    closeOpenedLessons()
                    performScroll()
                    unableToScroll()
                }
            }
        }
    }

    private fun FragmentTabsBinding.closeOpenedLessons() {
        rvLessons.children
            .filter { view -> view is MotionLayout }
            .filterNot { (it as MotionLayout).progress == 0f }
            .forEach {
                val position = rvLessons.getChildAdapterPosition(it)
                (it as MotionLayout).transitionToStart()
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(position - 1)
            }
    }

    private fun FragmentTabsBinding.performScroll() {
        if ((adapter).itemCount > 0) {
            val smoothScroller = LessonSmoothScroller(
                requireContext()
            )
            smoothScroller.targetPosition = 0
            rvLessons.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun FragmentTabsBinding.observeLessons() {
        observeResource<LessonListResource, LessonList>(viewModel.lessons, resourceView) {
            resourceView.setTryAgainAction { viewModel.refreshData() }
            if (it.isEmpty()) {
                setNoLessonsText()
                adapter.updateList(emptyList())
            } else {
                adapter.updateList(it)
            }
        }
    }

    private fun FragmentTabsBinding.setNoLessonsText() {
        context?.let { context ->
            resourceView.setMessageText(context.getString(R.string.no_lessons))
        }
    }

    private fun FragmentTabsBinding.setupRecycler() {
        rvLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val alphaAdapter = AlphaInAnimationAdapter(
                this@TabsFragment.adapter
            ).apply { setDuration(500) }
            adapter = alphaAdapter
            addOnRecyclerScrollListener()
            optimizeRecycler()
        }
    }

    private fun RecyclerView.optimizeRecycler() {
        setItemViewCacheSize((adapter as AlphaInAnimationAdapter).itemCount)
        isNestedScrollingEnabled = false
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

    companion object {
        private const val BOTTOM_SHEET_TAG = "bottom_sheet"

        const val ARG_COURSE = "course"
        const val ARG_GROUP = "group"
        const val ARG_WEEK = "week"
        const val ARG_SUBGROUPS = "subgroup"
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


private fun RecyclerView.addOnRecyclerScrollListener() {
    addOnScrollListener(LessonsScrollListener(this.adapter as AlphaInAnimationAdapter))
}

private fun Fragment.setupWindow() {
    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    activity?.window?.statusBarColor = resources.getColor(R.color.white_base_front, null)
}



