package com.kigya.unique.ui.tabs.callbacks

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kigya.unique.databinding.BottomSheetOptionsBinding
import kotlin.math.absoluteValue

class BottomSheetCallback(
    private val viewBinding: BottomSheetOptionsBinding,
    private val button: View,
    private val coordinator: CoordinatorLayout?
) : BottomSheetBehavior.BottomSheetCallback() {

    override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
        with(viewBinding) {
            if (slideOffset > 0) {
                button.translationY = 0f
                clCollapsedContainer.alpha = 1 - 2 * slideOffset
                clExpandedContainer.alpha = slideOffset * slideOffset
                when (slideOffset) {
                    in 0.0..0.5 -> if (clExpandedContainer.isVisible)
                        showCollapsedContainer() else Unit

                    in 0.5..1.0 -> showExpandableContainer()
                    else -> Unit
                }
            } else {
                button.y += slideOffset.absoluteValue * OFFSET_BUTTON_COEFFICIENT
                coordinator?.let {
                    it.y += slideOffset.absoluteValue * OFFSET_BUTTON_COEFFICIENT
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

    companion object {
        private const val OFFSET_BUTTON_COEFFICIENT = 3
    }
}