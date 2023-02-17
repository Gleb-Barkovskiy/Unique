package com.kigya.unique.ui.tabs.sheet.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.kigya.unique.R
import com.kigya.unique.databinding.BottomSheetTeacherBinding
import com.kigya.unique.ui.main.MainActivity
import com.kigya.unique.ui.tabs.main.TabsViewModel
import com.kigya.unique.utils.system.intent.IntentCreator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogTeacherFragment : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(BottomSheetTeacherBinding::bind)
    private val commonViewModel by viewModels<TabsViewModel>()
    private val dialogViewModel by viewModels<DialogTeacherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.bottom_sheet_teacher, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutParams()
        setSaveClickListener()
        setCloseClickListener()
        configureAutocompleteTextView()
    }

    private fun configureAutocompleteTextView() {
        with(viewBinding.mactvTeacherName) {
            val teacherList = dialogViewModel.teacherList
            val adapter = getAdapter(teacherList)
            setAdapter(adapter)
            threshold = 2
            dropDownAnchor = viewBinding.root.id
            addTextChangedListener(AutocompleteTextChangeListener(this, teacherList))
        }
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
                    dialogViewModel.teacherList,
                    successBlock = {
                        commonViewModel.setAccountType(if (viewBinding.ltStudent.isChecked) 0 else 1)
                        restartApp()
                    },
                    errorBlock = {
                        viewBinding.tilTeacher.error = context?.getString(R.string.no_teacher)
                    },
                )?.let { dialogViewModel.saveTeacher(it) }
            }
        }
    }

    private fun restartApp() =
        IntentCreator.createRestartIntent(requireActivity(), MainActivity::class.java)

    private fun initLayoutParams() {
        with(viewBinding) {
            ltTeacher.isChecked = true
            mactvTeacherName.setText(dialogViewModel.currentTeacher)
        }
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    companion object {
        fun newInstance() = DialogTeacherFragment()
    }
}
