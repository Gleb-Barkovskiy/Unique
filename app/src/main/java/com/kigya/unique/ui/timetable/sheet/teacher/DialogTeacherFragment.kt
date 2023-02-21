package com.kigya.unique.ui.timetable.sheet.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.kigya.unique.R
import com.kigya.unique.databinding.BottomSheetTeacherBinding
import com.kigya.unique.ui.main.MainActivity
import com.kigya.unique.ui.timetable.main.TimetableFragment
import com.kigya.unique.ui.timetable.main.TimetableViewModel
import com.kigya.unique.ui.timetable.sheet.teacher.listeners.AutocompleteTextChangeListener
import com.kigya.unique.utils.extensions.context.hideKeyboard
import com.kigya.unique.utils.helpers.TeacherValidator
import com.kigya.unique.utils.system.intent.IntentCreator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogTeacherFragment : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(BottomSheetTeacherBinding::bind)
    private val commonViewModel by viewModels<TimetableViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.bottom_sheet_teacher, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutParams()
        setInitialWeekState(arguments)
        setSaveClickListener()
        setCloseClickListener()
        hideElementsWhenRootClicked()
        closeFocusWhenSelected()
        closeFocusWhenDone()
        hidePopupWhenSideActionPerformed()
        configureAutocompleteTextView()
    }

    private fun hidePopupWhenSideActionPerformed() {
        with(viewBinding) {
            mactvTeacherName.setOnClickListener {
                psvSortByWeek.dismiss()
            }
            mactvTeacherName.setOnFocusChangeListener { _, _ -> psvSortByWeek.dismiss() }
        }
    }

    private fun closeFocusWhenDone() {
        viewBinding.mactvTeacherName.setOnEditorActionListener { _, actionId, _ ->
            if (EditorInfo.IME_ACTION_DONE == actionId || EditorInfo.IME_ACTION_UNSPECIFIED == actionId) {
                context?.hideKeyboard(viewBinding.mactvTeacherName)
                viewBinding.mactvTeacherName.clearFocus()
            }
            true
        }
    }

    private fun closeFocusWhenSelected() {
        viewBinding.mactvTeacherName.setOnItemClickListener { _, _, _, _ ->
            context?.hideKeyboard(viewBinding.mactvTeacherName)
            viewBinding.mactvTeacherName.clearFocus()
        }
    }

    private fun hideElementsWhenRootClicked() {
        with(viewBinding) {
            root.setOnClickListener {
                if (mactvTeacherName.isFocused) {
                    context?.hideKeyboard(mactvTeacherName)
                }
                psvSortByWeek.dismiss()
                viewBinding.mactvTeacherName.clearFocus()
            }
        }
    }

    private fun configureAutocompleteTextView() {
        with(viewBinding.mactvTeacherName) {
            val teacherList = commonViewModel.teacherList
            val adapter = getAdapter(teacherList)
            setAdapter(adapter)
            threshold = 2
            dropDownAnchor = viewBinding.root.id
            addTextChangedListener(AutocompleteTextChangeListener(this, teacherList))
        }
    }

    private fun setInitialWeekState(bundle: Bundle?) {
        viewBinding.psvSortByWeek.hint = bundle?.getString(TimetableFragment.ARG_WEEK)
    }

    private fun setCloseClickListener() {
        viewBinding.btnClose.setOnClickListener {
            commonViewModel.setAccountType(viewBinding.sstgMode.checkedId)
            dismiss()
        }
    }

    private fun MaterialAutoCompleteTextView.getAdapter(
        filteredSuggestions: List<String>,
    ) = ArrayAdapter(
        context,
        android.R.layout.simple_spinner_dropdown_item,
        filteredSuggestions,
    )

    private fun setSaveClickListener() {
        with(viewBinding) {
            btnSave.setOnClickListener {
                TeacherValidator(
                    viewBinding.mactvTeacherName.text.toString(),
                    commonViewModel.teacherList,
                    successBlock = {
                        commonViewModel.setAccountType(if (viewBinding.ltStudent.isChecked) 0 else 1)
                        restartApp()
                    },
                    errorBlock = {
                        viewBinding.tilTeacher.error = context?.getString(R.string.no_teacher)
                    },
                )?.let {
                    val isAutoModeEnabled =
                        if (psvSortByWeek.selectedIndex == -1) {
                            null
                        } else {
                            psvSortByWeek.selectedIndex == 0
                        }
                    commonViewModel.saveParams(it, isAutoModeEnabled)
                }
            }
        }
    }

    private fun restartApp() {
        IntentCreator.createRestartIntent(requireActivity(), MainActivity::class.java)
    }

    private fun initLayoutParams() {
        with(viewBinding) {
            ltTeacher.isChecked = true
            mactvTeacherName.setText(commonViewModel.savedTeacher)
        }
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    companion object {
        fun newInstance() = DialogTeacherFragment()
    }
}
