package com.beinmedia.test.models.network.control

import com.beinmedia.test.base.BaseRepository
import com.beinmedia.test.models.network.pojo.home.NewsResponse
import com.beinmedia.test.models.network.services.IHomeService
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.HttpException
import javax.inject.Inject

class RemoteRepository @Inject
constructor(private val serviceGenerator: ServiceGenerator) : BaseRepository(),IRestApiManager {


    override suspend fun getNews(searchKeyword:String): Resource<NewsResponse> {
        val homeService = serviceGenerator.createService(IHomeService::class.java)

        var res:Resource<NewsResponse>

        try {
//            val response = homeService.getNews("c64a0abb7cfbd0c8e85150d7dab62a67",searchKeyword)
            val response = homeService.getNews("7ae04a445f4b67178abe7b327b6ad3b9",searchKeyword)
            val pos = 0
            if (response.isSuccessful) {
                res = Resource.Success(data = response.body() as NewsResponse)
            } else {
                res = Resource.DataError(errorCode = response.code() as Int)
            }
        } catch (e: HttpException) {
            res = Resource.Exception(errorMessage = "error")
        } catch (e: Throwable) {
            res = Resource.Exception(errorMessage = "error")
        }
        return res
    }


}