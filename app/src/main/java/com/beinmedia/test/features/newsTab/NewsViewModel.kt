package com.beinmedia.test.features.newsTab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.beinmedia.test.base.BaseViewModel
import com.beinmedia.test.models.local.models.NewsDB
import com.beinmedia.test.models.manager.DataRepository
import com.beinmedia.test.models.network.control.Resource
import com.beinmedia.test.models.network.control.error.Error.Companion.NETWORK_ERROR
import com.beinmedia.test.models.network.control.error.ErrorManager
import com.beinmedia.test.models.network.control.error.mapper.ErrorMapper
import com.beinmedia.test.models.network.pojo.home.News
import com.beinmedia.test.models.network.pojo.home.NewsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class NewsViewModel @Inject constructor(
    val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext
) : BaseViewModel(), CoroutineScope {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var newsMutableLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()


    fun getNews(searchKeyWord: String) {
        launch {
            try {
                var serviceResponse: Resource<NewsResponse>? = dataRepository.getNews(searchKeyWord)
                serviceResponse?.data?.articles?.forEachIndexed { index,news ->
                    var isExist = false
                    var isDeleted = false
                    try {
                        withContext(Dispatchers.IO)
                        {
                            dataRepository.getNewNewsFromDb().forEach { dbItem ->
                                if (dbItem.url == news.url) {
                                    isExist = true
                                    isDeleted = dbItem.isDeleted
                                }
                            }

                            if (!isExist) {
                                val newsDb = NewsDB(
                                    title = news.title,
                                    description = news.description,
                                    content = news.content,
                                    deleteDate = "",
                                    image = news.image,
                                    isDeleted = false,
                                    publishedAt = news.publishedAt,
                                    url = news.url
                                )
                                dataRepository.insertNewsItem(newsDb)
                            } else {
                                if (isDeleted)
                                    serviceResponse?.data?.articles?.get(index)?.content = "ar"
                            }
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
                if (serviceResponse is Resource.Exception) {
                    withContext(Dispatchers.IO)
                    {
                        val newsLis = ArrayList<News>()
                        dataRepository.getNewNewsFromDb().forEach { dbItem ->
                            val news = News(
                                title = dbItem.title,
                                description = dbItem.description,
                                content = dbItem.content,
                                image = dbItem.image,
                                publishedAt = dbItem.publishedAt,
                                url = dbItem.url
                            )
                            newsLis.add(news)
                        }
                        val newsResponse = NewsResponse(
                            totalArticles = dataRepository.getNewNewsFromDb().size,
                            articles = newsLis
                        )
                        serviceResponse = Resource.Success(data = newsResponse)

                    }
                }
                newsMutableLiveData.postValue(serviceResponse!!)
            } catch (e: Exception) {
                newsMutableLiveData.postValue(Resource.DataError(NETWORK_ERROR))
            }
        }
    }

    fun moveItemToTrash(url: String) {
        launch {
            withContext(Dispatchers.IO)
            {
                dataRepository.getNewNewsFromDb().forEach { dbItem ->
                    if (dbItem.url == url) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val currentDate = sdf.format(Date())

                        dataRepository.moveToTrash(currentDate, dbItem.id)
                        val a = 0
                    }
                }
            }

        }
    }

    fun insertNewsItem(newsDb: NewsDB) {
        launch {
            withContext(Dispatchers.IO)
            {
                dataRepository.insertNewsItem(newsDb)
            }
        }
    }

}