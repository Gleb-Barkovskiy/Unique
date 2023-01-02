package com.kigya.unique.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.kigya.unique.R
import com.kigya.unique.databinding.PartResultViewBinding
import com.kigya.unique.utils.Resource
import com.kigya.unique.utils.exceptions.ConnectionException

class ResourceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: PartResultViewBinding
    private var tryAgainAction: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_result_view, this, true)
        binding = PartResultViewBinding.bind(this)
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
            Log.e(javaClass.simpleName, "Error", result.error)
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

