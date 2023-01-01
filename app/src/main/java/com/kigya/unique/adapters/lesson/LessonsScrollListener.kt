package com.kigya.unique.adapters.lesson

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class LessonsScrollListener(private val alphaAdapter: AlphaInAnimationAdapter) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val firstVisibleItemPosition =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItemPosition =
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        val visibleItemCount = lastVisibleItemPosition - firstVisibleItemPosition + 1
        val totalItemCount = recyclerView.adapter?.itemCount ?: 0
        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
            alphaAdapter.setDuration(0)
        } else {
            alphaAdapter.setDuration(500)
        }
    }
}