package com.beinmedia.test.dependencyinjection.module

import com.beinmedia.test.features.newsTab.NewsFragment
import com.beinmedia.test.features.trashTab.TrashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentModule {

    @ContributesAndroidInjector
    internal abstract fun contributeNewsFragment(): NewsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeTrashFragment(): TrashFragment
}