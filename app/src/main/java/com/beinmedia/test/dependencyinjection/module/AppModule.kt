package com.beinmedia.test.dependencyinjection.module

import android.app.Application
import androidx.room.Room
import com.beinmedia.test.models.local.LocalRepository
import com.beinmedia.test.models.local.TestRoomDatabase
import com.beinmedia.test.models.local.dao.NewsDBDao
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideLocalRepository(newsDBDao: NewsDBDao): LocalRepository {
        return LocalRepository(newsDBDao)
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.Main
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        TestRoomDatabase::class.java,
        "test_database"
    ).build()

    @Singleton
    @Provides
    fun provideNewsDao(db: TestRoomDatabase) = db.newsDBDao()

}