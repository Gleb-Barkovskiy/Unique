package com.kigya.unique.ui.tabs.sheet.teacher

import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@Suppress("UNCHECKED_CAST")
class AutocompleteTextChangeListener(
    private val autoCompleteTextView: MaterialAutoCompleteTextView,
    private val list: List<String>,
) : TextWatcher {

    private var filteredSuggestions by Delegates.notNull<List<String>>()

    init {
        filteredSuggestions = list
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        with(autoCompleteTextView) {
            var lastFilterJob: Job? = null
            doOnTextChanged { text, _, _, _ ->
                lastFilterJob?.cancel()
                lastFilterJob = CoroutineScope(kotlinx.coroutines.Dispatchers.Main + Job()).launch {
                    filteredSuggestions =
                        list.filter { it.contains(text.toString(), ignoreCase = true) }
                    notifyAdapter(
                        autoCompleteTextView.adapter as ArrayAdapter<String>,
                        filteredSuggestions,
                    )
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit

    private fun notifyAdapter(
        adapter: ArrayAdapter<String>,
        filteredSuggestions: List<String>,
    ) {
        adapter.apply {
            clear()
            addAll(filteredSuggestions)
            notifyDataSetChanged()
        }
    }
}
