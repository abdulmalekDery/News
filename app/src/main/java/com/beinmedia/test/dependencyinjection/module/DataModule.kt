package com.beinmedia.test.dependencyinjection.module

import com.beinmedia.test.models.manager.DataRepository
import com.beinmedia.test.models.manager.DataSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(dataRepository: DataRepository): DataSource
}