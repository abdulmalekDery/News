package com.beinmedia.test.models.manager

import com.beinmedia.test.models.local.models.NewsDB
import com.beinmedia.test.models.network.control.Resource
import com.beinmedia.test.models.network.pojo.home.NewsResponse
import kotlinx.coroutines.flow.Flow


interface DataSource {
    suspend fun getNews(searchKeyword:String): Resource<NewsResponse>
    fun getNewsFromDb(): Flow<List<NewsDB>>
    suspend fun insertNewsItem(newsDB: NewsDB)
    suspend fun moveToTrash(deleteDate: String, id: Int)
    fun getDeletedNews(): Flow<List<NewsDB>>
    suspend fun deletePermanently(id: Int)
    fun getNewNewsFromDb(): List<NewsDB>
}
