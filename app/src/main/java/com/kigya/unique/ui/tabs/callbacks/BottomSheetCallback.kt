package com.kigya.unique.ui.tabs.callbacks

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kigya.unique.databinding.BottomSheetOptionsBinding
import kotlin.math.absoluteValue

class BottomSheetCallback(
    private val viewBinding: BottomSheetOptionsBinding,
    private val coordinator: CoordinatorLayout?
) : BottomSheetBehavior.BottomSheetCallback() {

    override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        with(viewBinding) {
            if (slideOffset > 0) {
                clCollapsedContainer.alpha = 1 - 2 * slideOffset
                clExpandedContainer.alpha = slideOffset * slideOffset
                when (slideOffset) {
                    in 0.0..0.5 -> if (clExpandedContainer.isVisible)
                        showCollapsedContainer() else Unit

                    in 0.5..1.0 -> showExpandableContainer()
                    else -> Unit
                }
            } else {
                coordinator?.let {
                    it.y += slideOffset.absoluteValue
                }
            }
        }
    }

    private fun BottomSheetOptionsBinding.showCollapsedContainer() {
        clCollapsedContainer.visibility = View.VISIBLE
        clExpandedContainer.visibility = View.INVISIBLE
    }

    private fun BottomSheetOptionsBinding.showExpandableContainer() {
        clCollapsedContainer.visibility = View.GONE
        clExpandedContainer.visibility = View.VISIBLE
    }
}