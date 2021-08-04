package com.beinmedia.test.models.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beinmedia.test.models.local.models.NewsDB
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDBDao {

    @Query("SELECT * FROM news_table ORDER BY title ASC")
    fun getNews(): Flow<List<NewsDB>>

    @Query("SELECT * FROM news_table ORDER BY title ASC")
    fun getNewNews(): List<NewsDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(feeds: ArrayList<NewsDB>): LongArray

    @Query("DELETE FROM news_table")
    suspend fun deleteAll()

    @Query("DELETE FROM news_table WHERE id = :id")
    suspend fun deleteNewsFromTrash(id: Int)

    @Query("UPDATE news_table SET isDeleted =:isDeleted, deleteDate= :deletedDate  WHERE id = :id")
    suspend fun moveToTrash(id: Int, deletedDate: String, isDeleted: Boolean = true)

    @Query("SELECT * FROM news_table WHERE isDeleted =:isDeleted")
    fun getDeletedNews(isDeleted: Boolean = true):  Flow<List<NewsDB>>
}