package com.beinmedia.test.models.manager

import com.beinmedia.test.models.local.LocalRepository
import com.beinmedia.test.models.local.models.NewsDB
import com.beinmedia.test.models.network.control.RemoteRepository
import com.beinmedia.test.models.network.control.Resource
import com.beinmedia.test.models.network.pojo.home.NewsResponse
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject


class DataRepository @Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : DataSource {

    override suspend fun getNews(searchKeyword:String): Resource<NewsResponse> {
        return remoteRepository.getNews(searchKeyword)
    }

    override fun getNewsFromDb(): Flow<List<NewsDB>> {
        return localRepository.getNonDeletedNews()
    }

    override fun getNewNewsFromDb(): List<NewsDB> {
        return localRepository.getNewNonDeletedNews()
    }

    override fun getDeletedNews(): Flow<List<NewsDB>> {
        return localRepository.getDeletedNews()
    }

    override suspend fun insertNewsItem(newsDB: NewsDB) {
        localRepository.insertNewsItem(newsDB)
    }

    override suspend fun moveToTrash(deleteDate: String, id: Int){
        localRepository.moveToTrash(deleteDate,id)
    }

    override suspend fun deletePermanently(id: Int) {
        localRepository.deletePermanently(id)
    }
}
