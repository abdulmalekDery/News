package com.beinmedia.test.dependencyinjection.module

import com.beinmedia.test.models.network.control.error.ErrorManager
import com.beinmedia.test.models.network.control.error.mapper.ErrorFactory
import com.beinmedia.test.models.network.control.error.mapper.ErrorMapper
import com.beinmedia.test.models.network.control.error.mapper.ErrorMapperInterface
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorFactory

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperInterface
}
