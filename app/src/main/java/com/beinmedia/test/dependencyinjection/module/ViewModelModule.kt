package com.beinmedia.test.dependencyinjection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beinmedia.test.base.ViewModelFactory
import com.beinmedia.test.dependencyinjection.utils.ViewModelKey
import com.beinmedia.test.features.home.MainActivityViewModel
import com.beinmedia.test.features.newsTab.NewsViewModel
import com.beinmedia.test.features.trashTab.TrashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrashViewModel::class)
    fun bindTrashViewModel(viewModel: TrashViewModel): ViewModel


    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}