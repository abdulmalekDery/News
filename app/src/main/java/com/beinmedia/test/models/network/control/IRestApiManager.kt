package com.beinmedia.test.models.network.control

import com.beinmedia.test.models.network.pojo.home.NewsResponse


internal interface IRestApiManager {
    suspend fun getNews(searchKeyword:String): Resource<NewsResponse>

}