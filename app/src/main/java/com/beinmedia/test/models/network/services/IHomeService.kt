package com.beinmedia.test.models.network.services

import com.beinmedia.test.config.Urls
import com.beinmedia.test.models.network.control.ResponseWrapper
import com.beinmedia.test.models.network.pojo.home.NewsResponse
import retrofit2.Response
import retrofit2.http.*

interface IHomeService {

    @GET(Urls.SEARCH_NEWS)
    suspend fun getNews(
        @Query("token") token:String,
        @Query("q") searchKeyword:String
    ): Response<NewsResponse>
}