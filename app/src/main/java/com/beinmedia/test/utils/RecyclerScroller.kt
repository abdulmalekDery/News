package com.beinmedia.test.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerScroller(val show: (() -> Unit)? = null,
                       val hide: (() -> Unit)? = null,
                       val onLoadMore: ((page: Int) -> Unit)? = null) :
        androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

    //this params to detect scroll down
    var scrollDist = 0
    var isVisible = true
    val MINIMUM = 50

    //this params to detect load more data
    private var currentPage = 0
    private var mPreviousTotal = 0
    private var mLoading = true

    init {
        currentPage = 0
    }

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //this code to detect scroll down
        if (isVisible && scrollDist > MINIMUM) {
            hide?.let { it() }
            scrollDist = 0
            isVisible = false
        } else if (!isVisible && scrollDist < -MINIMUM) {
            show?.let { it() }
            scrollDist = 0
            isVisible = true
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy
        }

        //this code to detect load more data
        val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = recyclerView.layoutManager!!.itemCount
        val firstVisibleItem = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false
                mPreviousTotal = totalItemCount
            }
        }
        val visibleThreshold = 5
        if (!mLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore?.let { it(currentPage) }
            mLoading = true
        }
    }
}
