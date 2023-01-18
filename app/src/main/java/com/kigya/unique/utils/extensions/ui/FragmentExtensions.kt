package com.kigya.unique.utils.extensions.ui

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.kigya.unique.R
import com.kigya.unique.ui.base.BaseFragment
import com.kigya.unique.ui.views.ResourceView
import com.kigya.unique.utils.wrappers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
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

fun <T> DialogFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
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
    resourceView.setResource(result as Resource<*>)
    if (result is Resource.Success<*>) {
        @Suppress("UNCHECKED_CAST")
        onSuccess(result.data as R)
    }
}