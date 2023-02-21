package com.kigya.unique.ui.timetable.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.adapters.calendar.CalendarWeekdayBinder
import com.kigya.unique.adapters.calendar.interlayers.CalendarDateBinder
import com.kigya.unique.adapters.calendar.interlayers.CalendarWeekdayClickListener
import com.kigya.unique.adapters.lesson.LessonAdapter
import com.kigya.unique.adapters.lesson.interlayers.LessonsScrollListener
import com.kigya.unique.databinding.FragmentTabsBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.ui.timetable.sheet.student.DialogStudentFragment
import com.kigya.unique.ui.timetable.sheet.teacher.DialogTeacherFragment
import com.kigya.unique.utils.LessonList
import com.kigya.unique.utils.LessonListResource
import com.kigya.unique.utils.helpers.CalendarHelper
import com.kigya.unique.utils.helpers.CalendarHelper.currentDate
import com.kigya.unique.utils.helpers.CalendarHelper.daysOfWeek
import com.kigya.unique.utils.helpers.CalendarHelper.endDate
import com.kigya.unique.utils.helpers.CalendarHelper.getWeekStringValueAbbreviation
import com.kigya.unique.utils.helpers.CalendarHelper.startDate
import com.kigya.unique.utils.extensions.context.onTouchResponseVibrate
import com.kigya.unique.utils.extensions.ui.collectFlow
import com.kigya.unique.utils.extensions.ui.observeResource
import com.kigya.unique.utils.extensions.ui.view.setOnSidesSwipeTouchListener
import com.kigya.unique.utils.extensions.ui.view.startCenterCircularReveal
import com.kigya.unique.utils.extensions.ui.view.startSidesCircularReveal
import com.kigya.unique.utils.mappers.LocaleConverter.Russian.russianShortValue
import com.kizitonwose.calendar.view.DaySize
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.time.LocalDate

@AndroidEntryPoint
class TimetableFragment : BaseFragment(R.layout.fragment_tabs), CalendarDateBinder {

    private val viewBinding by viewBinding(FragmentTabsBinding::bind)
    override val viewModel by viewModels<TimetableViewModel>()
    private val adapter by lazy { LessonAdapter(this.requireActivity(), viewModel.isStudentMode()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.startCenterCircularReveal()
        setupWindow()
        setupCalendarView(viewModel, this@TimetableFragment)
        setupRecycler()
        observeLessons()
        observeScroll()
        addShowOptionsClickListener()
        setCurrentWeekSerialNumberValue()
        setOnSidesSwipeTouchListener()
    }

    private fun setOnSidesSwipeTouchListener() {
        with(viewBinding) {
            root.setOnSidesSwipeTouchListener(
                leftAction = { viewModel.toPreviousDay(calendarView) },
                rightAction = { viewModel.toNextDay(calendarView) },
            )
        }
    }

    private fun setCurrentWeekSerialNumberValue() {
        viewBinding.tvCurrentWeek.text = getWeekStringValueAbbreviation(viewModel.selectedDate)
    }

    private fun addShowOptionsClickListener() {
        viewBinding.btnOptions.setOnClickListener {
            requireActivity().onTouchResponseVibrate {
                if (viewModel.isStudentMode()) {
                    openStudentBottomSheet()
                } else {
                    openTeacherBottomSheet()
                }
            }
        }
    }

    private fun openStudentBottomSheet() {
        val bottomFragment = DialogStudentFragment.newInstance()
        with(viewModel) {
            bottomFragment.arguments = Bundle().apply {
                putString(ARG_COURSE, getCourseHint())
                putString(ARG_GROUP, getGroupHint())
                putString(ARG_WEEK, getWeekModeHint())
                putStringArrayList(ARG_SUBGROUPS, ArrayList(subgroupList))
            }
        }
        bottomFragment.show(parentFragmentManager, BOTTOM_SHEET_STUDENT_TAG)
    }

    private fun openTeacherBottomSheet() {
        val bottomFragment = DialogTeacherFragment.newInstance()
        bottomFragment.arguments = Bundle().apply {
            putString(ARG_WEEK, viewModel.getWeekModeHint())
        }
        bottomFragment.show(parentFragmentManager, BOTTOM_SHEET_TEACHER_TAG)
    }

    private fun observeScroll() {
        collectFlow(viewModel.shouldScroll) { shouldScroll ->
            if (shouldScroll) {
                closeOpenedLessons()
            }
        }
    }

    private fun closeOpenedLessons() {
        with(viewBinding) {
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
    }

    private fun observeLessons() {
        with(viewBinding) {
            observeResource<LessonListResource, LessonList>(viewModel.lessons, resourceView) {
                rvLessons.startSidesCircularReveal(true)
                setCurrentWeekSerialNumberValue()
                if (it.isEmpty()) {
                    setNoLessonsText()
                    adapter.submitList(emptyList())
                } else {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun setNoLessonsText() {
        context?.let { context ->
            viewBinding.resourceView.setMessageText(context.getString(R.string.no_lessons))
        }
    }

    private fun setupRecycler() {
        with(viewBinding) {
            rvLessons.apply {
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = null
                val alphaAdapter =
                    AlphaInAnimationAdapter(this@TimetableFragment.adapter).apply {
                        setDuration(ALPHA_IN_ANIMATION_ADAPTER_DURATION)
                    }
                adapter = alphaAdapter
                addOnRecyclerScrollListener(this)
                optimizeRecycler(this)
            }
        }
    }

    private fun optimizeRecycler(recyclerView: RecyclerView) {
        with(recyclerView) {
            setItemViewCacheSize((adapter as AlphaInAnimationAdapter).itemCount)
            isNestedScrollingEnabled = false
        }
    }

    override fun bindDate(
        date: LocalDate,
        tvDate: TextView,
        tvWeekDay: TextView,
        wrapper: ViewGroup,
    ) {
        tvDate.text = date.dayOfMonth.toString()
        tvWeekDay.text = date.dayOfWeek.russianShortValue()
        if (date == viewModel.previousSelectedDate) {
            CalendarHelper.setCalendarInactiveCalendarParams(tvDate, tvWeekDay, wrapper)
        }
        if (date == viewModel.selectedDate) {
            CalendarHelper.setCalendarActiveViewParams(tvDate, tvWeekDay, wrapper)
        } else {
            CalendarHelper.setCalendarInactiveCalendarParams(tvDate, tvWeekDay, wrapper)
        }
    }

    private fun setupCalendarView(
        listener: CalendarWeekdayClickListener,
        dateBinder: CalendarDateBinder,
    ) {
        with(viewBinding) {
            calendarView.apply {
                dayBinder = CalendarWeekdayBinder(calendarView, listener, dateBinder)
                setup(startDate, endDate, daysOfWeek.first())
                daySize = DaySize.SeventhWidth
                scrollToDate(currentDate)
            }
        }
    }

    private fun addOnRecyclerScrollListener(recyclerView: RecyclerView) {
        recyclerView.apply {
            addOnScrollListener(LessonsScrollListener(this.adapter as AlphaInAnimationAdapter))
        }
    }

    private fun setupWindow() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor = resources.getColor(R.color.white_base_front, null)
    }

    companion object {
        private const val BOTTOM_SHEET_STUDENT_TAG = "bottom_sheet_student"
        private const val BOTTOM_SHEET_TEACHER_TAG = "bottom_sheet_teacher"

        const val ARG_COURSE = "course"
        const val ARG_GROUP = "group"
        const val ARG_WEEK = "week"
        const val ARG_SUBGROUPS = "subgroup"

        private const val ALPHA_IN_ANIMATION_ADAPTER_DURATION = 500
    }
}
