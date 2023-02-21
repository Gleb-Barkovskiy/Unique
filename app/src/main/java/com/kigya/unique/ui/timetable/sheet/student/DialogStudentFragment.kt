package com.kigya.unique.ui.timetable.sheet.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kigya.unique.R
import com.kigya.unique.databinding.BottomSheetStudentBinding
import com.kigya.unique.ui.main.MainActivity
import com.kigya.unique.ui.timetable.main.TimetableFragment
import com.kigya.unique.ui.timetable.main.TimetableViewModel
import com.kigya.unique.utils.constants.ModelConst.SUBGROUP_A
import com.kigya.unique.utils.constants.ModelConst.SUBGROUP_B
import com.kigya.unique.utils.constants.ModelConst.SUBGROUP_C
import com.kigya.unique.utils.constants.ModelConst.SUBGROUP_D
import com.kigya.unique.utils.mappers.FiltersMapper.getResourceByCourse
import com.kigya.unique.utils.system.intent.IntentCreator
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogStudentFragment : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(BottomSheetStudentBinding::bind)
    private val commonViewModel by viewModels<TimetableViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.bottom_sheet_student, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialArgs()
        setSaveClickListener()
        setCloseClickListener()
        setOnCourseClickListener()
        hideElementsWhenRootClicked()
    }

    private fun setOnCourseClickListener() {
        with(viewBinding) {
            psvSortByCourse.setOnSpinnerItemSelectedListener(
                OnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
                    psvSortByGroup.setItems(getResourceByCourse(newIndex))
                    psvSortByGroup.dismiss()
                    val allowedList = resources.getStringArray(getResourceByCourse(newIndex))
                    if (!allowedList.contains(psvSortByGroup.text)) {
                        psvSortByGroup.selectItemByIndex(1)
                    }
                },
            )
        }
    }

    private fun hideElementsWhenRootClicked() {
        with(viewBinding) {
            root.setOnClickListener {
                psvSortByCourse.dismiss()
                psvSortByGroup.dismiss()
                psvSortByWeek.dismiss()
            }
        }
    }

    private fun setSaveClickListener() {
        with(viewBinding) {
            btnSave.setOnClickListener {
                commonViewModel.setParams(
                    if (psvSortByCourse.selectedIndex == -1) null else psvSortByCourse.selectedIndex + 1,
                    if (psvSortByGroup.selectedIndex == -1) null else psvSortByGroup.selectedIndex,
                    listOfNotNull(
                        if (ltSubgroupA.isChecked) SUBGROUP_A else null,
                        if (ltSubgroupB.isChecked) SUBGROUP_B else null,
                        if (ltSubgroupC.isChecked) SUBGROUP_C else null,
                        if (ltSubgroupD.isChecked) SUBGROUP_D else null,
                    ),
                    if (psvSortByWeek.selectedIndex == -1) null else psvSortByWeek.selectedIndex == 0,
                )
                commonViewModel.setAccountType(if (viewBinding.ltStudent.isChecked) 0 else 1)
                restartApp()
            }
        }
    }

    private fun setCloseClickListener() {
        viewBinding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun restartApp() {
        IntentCreator.createRestartIntent(requireActivity(), MainActivity::class.java)
    }

    private fun setInitialArgs() {
        with(arguments) {
            setInitialCourseState(this)
            setInitialGroupState(this)
            setInitialWeekState(this)
            setInitialActiveSubgroups(this)
            setStudentAccountMode()
        }
    }

    private fun setInitialActiveSubgroups(bundle: Bundle?): Unit? =
        with(viewBinding) {
            bundle?.getStringArrayList(TimetableFragment.ARG_SUBGROUPS)?.forEach {
                when (it) {
                    SUBGROUP_A -> ltSubgroupA.isChecked = true
                    SUBGROUP_B -> ltSubgroupB.isChecked = true
                    SUBGROUP_C -> ltSubgroupC.isChecked = true
                    SUBGROUP_D -> ltSubgroupD.isChecked = true
                }
            }
        }

    private fun setStudentAccountMode() {
        viewBinding.ltStudent.isChecked = true
    }

    private fun setInitialWeekState(bundle: Bundle?) {
        viewBinding.psvSortByWeek.hint = bundle?.getString(TimetableFragment.ARG_WEEK)
    }

    private fun setInitialGroupState(bundle: Bundle?) {
        viewBinding.psvSortByGroup.hint = bundle?.getString(TimetableFragment.ARG_GROUP)
    }

    private fun setInitialCourseState(bundle: Bundle?) {
        viewBinding.psvSortByCourse.hint = bundle?.getString(TimetableFragment.ARG_COURSE)
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    companion object {
        fun newInstance() = DialogStudentFragment()
    }
}
