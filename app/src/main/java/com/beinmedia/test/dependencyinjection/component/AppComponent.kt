package com.beinmedia.test.dependencyinjection.component

import android.app.Application
import com.beinmedia.test.App
import com.beinmedia.test.dependencyinjection.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        DataModule::class,
        ErrorModule::class,
        ActivityModuleBuilder::class,
        ServiceModule::class,
        ViewModelModule::class,
        FragmentModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }

    fun inject(app: App)
}
