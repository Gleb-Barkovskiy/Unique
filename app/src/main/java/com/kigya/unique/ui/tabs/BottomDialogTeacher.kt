package com.kigya.unique.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kigya.unique.R
import com.kigya.unique.databinding.BottomSheetTeacherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogTeacher : BottomSheetDialogFragment() {

    private val viewBinding by viewBinding(BottomSheetTeacherBinding::bind)
    private val viewModel by viewModels<TabsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.bottom_sheet_teacher, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCloseClickListener()
    }

    private fun setCloseClickListener() {
        viewBinding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    companion object {
        fun newInstance() = BottomDialogTeacher()
    }
}
