package com.beinmedia.test.models.local

import androidx.annotation.WorkerThread
import com.beinmedia.test.models.local.dao.NewsDBDao
import com.beinmedia.test.models.local.models.NewsDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject
constructor(
    private val newsDBDao: NewsDBDao
) {

    fun getNonDeletedNews(): Flow<List<NewsDB>>{
        return newsDBDao.getNews()
    }

    fun getNewNonDeletedNews(): List<NewsDB>{
        return newsDBDao.getNewNews()
    }
    suspend fun moveToTrash(deleteDate: String, id: Int) {
        return newsDBDao.moveToTrash(id, deleteDate)
    }

    fun insertNewsList(newsDBList: ArrayList<NewsDB>): LongArray {
        return newsDBDao.insertAll(newsDBList)
    }

//    @WorkerThread
    suspend fun insertNewsItem(newsDB: NewsDB) {
        newsDBDao.insert(newsDB)
    }

    fun getDeletedNews(): Flow<List<NewsDB>> {
        return newsDBDao.getDeletedNews()
    }

    suspend fun deletePermanently(id: Int) {
        newsDBDao.deleteNewsFromTrash(id)
    }

    suspend fun deleteAllNews(){
        newsDBDao.deleteAll()
    }

}