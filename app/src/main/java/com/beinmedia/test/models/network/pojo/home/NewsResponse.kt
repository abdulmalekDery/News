package com.beinmedia.test.models.network.pojo.home

import com.google.gson.annotations.SerializedName

data class NewsResponse (
    @SerializedName("totalArticles") val totalArticles : Int,
    @SerializedName("articles") val articles : List<News>
)