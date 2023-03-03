package com.kigya.unique.utils.extensions.ui.flow

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> ViewModel.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {
    viewModelScope.launch {
        flow.collect {
            onCollect(it)
        }
    }
}
