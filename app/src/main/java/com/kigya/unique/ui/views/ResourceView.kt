package com.kigya.unique.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kigya.unique.R
import com.kigya.unique.utils.Resource
import com.kigya.unique.databinding.PartResultViewBinding
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.utils.exceptions.ConnectionException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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

    fun <T> setResource(fragment: BaseFragment, result: Resource<T>) {
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

fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                onCollect(it)
            }
        }
    }
}

fun <T, R> BaseFragment.observeResource(
    flow: Flow<T>,
    resourceView: ResourceView,
    onSuccess: (R) -> Unit
) = collectFlow(flow) { result ->
    resourceView.setResource(this, result as Resource<*>)
    if (result is Resource.Success<*>) {
        @Suppress("UNCHECKED_CAST")
        onSuccess(result.data as R)
    }
}


