package com.kigya.unique.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.unique.R
import com.kigya.unique.databinding.PartResultViewBinding
import com.kigya.unique.utils.exceptions.ConnectionException
import com.kigya.unique.utils.wrappers.Resource

class ResourceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: PartResultViewBinding by viewBinding(PartResultViewBinding::bind)
    private var tryAgainAction: (() -> Unit)? = null

    init {
        inflate(context, R.layout.part_result_view, this)
    }

    fun setTryAgainAction(action: () -> Unit) {
        this.tryAgainAction = action
    }

    fun setMessageText(message: String) {
        with(binding) {
            tvErrorMessage.apply {
                isVisible = true
                text = message
            }
        }
    }

    fun <T> setResource(result: Resource<T>) {
        binding.tvErrorMessage.isVisible = result is Resource.Error<*>
        binding.btnError.isVisible = result is Resource.Error<*>
        binding.progressBar.isVisible = result is Resource.Loading<*>
        if (result is Resource.Error) {
            val message = when (result.error) {
                is ConnectionException -> context.getString(R.string.connection_error)
                else -> context.getString(R.string.internal_error)
            }
            binding.tvErrorMessage.text = message
            renderTryAgainButton()
        }
    }

    private fun renderTryAgainButton() {
        binding.btnError.setOnClickListener { tryAgainAction?.invoke() }
    }
}
