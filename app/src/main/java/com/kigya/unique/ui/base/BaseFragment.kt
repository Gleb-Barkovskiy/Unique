package com.kigya.unique.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    abstract val viewModel: BaseViewModel
}
