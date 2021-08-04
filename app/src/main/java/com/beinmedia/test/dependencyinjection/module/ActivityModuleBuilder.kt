package com.beinmedia.test.dependencyinjection.module

import com.beinmedia.test.features.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModuleBuilder {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity


}