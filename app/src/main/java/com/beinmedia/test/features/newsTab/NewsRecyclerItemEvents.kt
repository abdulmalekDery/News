package com.beinmedia.test.features.newsTab

import android.graphics.Bitmap
import com.beinmedia.test.models.network.pojo.home.News

interface NewsRecyclerItemEvents {

    fun onItemClicked(item : News, index : Int)
    fun onShareClicked(item: News, bitmapFromView: Bitmap?)
}