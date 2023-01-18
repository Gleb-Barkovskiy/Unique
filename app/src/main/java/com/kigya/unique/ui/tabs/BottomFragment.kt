package com.kigya.unique.ui.tabs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kigya.unique.R
import com.kigya.unique.databinding.BottomSheetOptionsBinding
import com.kigya.unique.ui.tabs.callbacks.BottomSheetCallback


class BottomFragment : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(BottomSheetOptionsBinding::bind)

    override fun onStart() {
        super.onStart()
        setupDialogBehavior()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(LAYOUT_RES, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            setInitialArgs()
        }
    }

    private fun BottomSheetOptionsBinding.setInitialArgs() {
        with(arguments) {
            setInitialCourseState(this@with)
            setInitialGroupState(this@with)
            setInitialWeekState(this@with)
            setInitialActiveSubgroups(this@with)
        }
    }

    private fun BottomSheetOptionsBinding.setInitialActiveSubgroups(bundle: Bundle?): Unit? =
        bundle?.getStringArrayList(TabsFragment.ARG_SUBGROUPS)?.forEach {
            when (it) {
                SUBGROUP_A -> ltSubgroupA.isChecked = true
                SUBGROUP_B -> ltSubgroupB.isChecked = true
                SUBGROUP_C -> ltSubgroupC.isChecked = true
            }
        }

    private fun BottomSheetOptionsBinding.setInitialWeekState(bundle: Bundle?) {
        psvSortByWeek.hint = bundle?.getString(TabsFragment.ARG_WEEK)
    }

    private fun BottomSheetOptionsBinding.setInitialGroupState(bundle: Bundle?) {
        psvSortByGroup.hint = bundle?.getString(TabsFragment.ARG_GROUP)
    }

    private fun BottomSheetOptionsBinding.setInitialCourseState(bundle: Bundle?) {
        psvSortByCourse.hint = bundle?.getString(TabsFragment.ARG_COURSE)
    }

    private fun setupDialogBehavior() {
        val density = getDensity()
        dialog?.let { dialog ->
            val bottomSheet = getBottomSheet(dialog)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            setInitialState(behavior, density)
            val (coordinator, button) = addButton(dialog, density)
            behavior.addBottomSheetCallback(BottomSheetCallback(viewBinding, button, coordinator))
        }
    }

    private fun setInitialState(behavior: BottomSheetBehavior<FrameLayout>, density: Float) {
        behavior.apply {
            peekHeight = (COLLAPSED_HEIGHT * density).toInt()
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun addButton(
        dialog: Dialog,
        density: Float
    ): Pair<CoordinatorLayout?, View> {
        val coordinator = getCoordinator(dialog)
        val containerLayout = getContainerLayout(dialog)

        val button = getButton(dialog)

        button.let {
            setButtonLayoutParams(it, density)
            containerLayout?.addView(it)
            setButtonSize(it, coordinator, button, density, containerLayout)
        }

        return Pair(coordinator, button)
    }

    private fun setButtonSize(
        it: View,
        coordinator: CoordinatorLayout?,
        button: View,
        density: Float,
        containerLayout: FrameLayout?
    ) = it.post {
        (coordinator?.layoutParams as ViewGroup.MarginLayoutParams).apply {
            it.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            this.bottomMargin = (button.measuredHeight - 8 * density).toInt()
            containerLayout?.requestLayout()
        }
    }

    private fun setButtonLayoutParams(it: View, density: Float) {
        it.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            height = (60 * density).toInt()
            gravity = Gravity.BOTTOM
        }
    }

    @SuppressLint("InflateParams")
    private fun getButton(dialog: Dialog): View =
        dialog.layoutInflater.inflate(R.layout.button_options, null)

    private fun getContainerLayout(dialog: Dialog): FrameLayout? =
        dialog.findViewById<FrameLayout>(com.google.android.material.R.id.container)

    private fun getCoordinator(dialog: Dialog) =
        (dialog as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)

    private fun getBottomSheet(dialog: Dialog) =
        dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

    private fun getDensity() = requireContext().resources.displayMetrics.density

    override fun getTheme() = DIALOG_THEME_RES

    companion object {
        fun newInstance() = BottomFragment()

        private const val COLLAPSED_HEIGHT = 228
        private val DIALOG_THEME_RES = R.style.AppBottomSheetDialogTheme
        private val LAYOUT_RES = R.layout.bottom_sheet_options

        private const val SUBGROUP_A = "а"
        private const val SUBGROUP_B = "б"
        private const val SUBGROUP_C = "в"
    }
}