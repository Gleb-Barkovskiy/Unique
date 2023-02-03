package com.kigya.unique.ui.tabs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kigya.unique.R
import com.kigya.unique.databinding.BottomSheetOptionsBinding
import com.kigya.unique.ui.main.MainActivity
import com.kigya.unique.ui.tabs.callbacks.BottomSheetCallback
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomFragment : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(BottomSheetOptionsBinding::bind)
    private val viewModel by viewModels<TabsViewModel>()

    override fun onStart() {
        super.onStart()
        setupDialogBehavior()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            setInitialArgs()
            setSaveClickListener()
            setOnCourseClickListener()
        }
    }

    private fun BottomSheetOptionsBinding.setOnCourseClickListener() {
        psvSortByCourse.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
            psvSortByGroup.setItems(getResourceByCourse(newIndex))
            psvSortByGroup.dismiss()
            val allowedList = resources.getStringArray(getResourceByCourse(newIndex))
            if (!allowedList.contains(psvSortByGroup.text)) {
                psvSortByGroup.selectItemByIndex(1)
            }
        })
    }


    private fun getResourceByCourse(course: Int): Int = when (course) {
        0 -> R.array.group_options_1
        1 -> R.array.group_options_2
        2 -> R.array.group_options_3
        3 -> R.array.group_options_4
        else -> R.array.group_options_1
    }


    private fun BottomSheetOptionsBinding.setSaveClickListener() {
        btnSave.setOnClickListener {
            viewModel.setParams(
                if (psvSortByCourse.selectedIndex == -1) null else psvSortByCourse.selectedIndex + 1,
                if (psvSortByGroup.selectedIndex == -1) null else psvSortByGroup.selectedIndex,
                listOfNotNull(
                    if (ltSubgroupA.isChecked) SUBGROUP_A else null,
                    if (ltSubgroupB.isChecked) SUBGROUP_B else null,
                    if (ltSubgroupC.isChecked) SUBGROUP_C else null,
                ),
                if (psvSortByWeek.selectedIndex == -1) null else psvSortByWeek.selectedIndex == 0
            )
            restartApp()
        }
    }

    private fun restartApp() {
        val intent = Intent(activity?.applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_in)
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
        val courseString = bundle?.getString(TabsFragment.ARG_COURSE)
        val courseInt = courseString?.split(" ")?.get(1)?.toInt()
        psvSortByCourse.hint = bundle?.getString(TabsFragment.ARG_COURSE)
        psvSortByGroup.setItems(getResourceByCourse(courseInt ?: 0))
    }

    private fun setupDialogBehavior() {
        val density = getDensity()
        dialog?.let { dialog ->
            val bottomSheet = getBottomSheet(dialog)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            setInitialState(behavior, density)
            behavior.addBottomSheetCallback(
                BottomSheetCallback(
                    viewBinding,
                    getCoordinator(dialog)
                )
            )
        }
    }

    private fun setInitialState(behavior: BottomSheetBehavior<FrameLayout>, density: Float) {
        behavior.apply {
            peekHeight = calculateDialogPeekHeight(density)
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun calculateDialogPeekHeight(density: Float) =
        (COLLAPSED_HEIGHT * density + viewBinding.clVisibleContainer.paddingBottom + viewBinding.clParent.paddingBottom).toInt()

    private fun getCoordinator(dialog: Dialog) =
        (dialog as BottomSheetDialog).findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)

    private fun getBottomSheet(dialog: Dialog) =
        dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

    private fun getDensity() = requireContext().resources.displayMetrics.density

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    companion object {
        fun newInstance() = BottomFragment()

        private const val COLLAPSED_HEIGHT = 228

        private const val SUBGROUP_A = "а"
        private const val SUBGROUP_B = "б"
        private const val SUBGROUP_C = "в"
    }
}