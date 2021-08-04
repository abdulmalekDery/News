package com.beinmedia.test

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.beinmedia.test.base.BaseActivity
import com.beinmedia.test.dependencyinjection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.*
import javax.inject.Inject


class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initDagger()
        initLocale()

    }

    private fun initLocale() {

    }

    fun getCurrentLocale(context: Context): Locale? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    open fun initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
    }
    companion object {
        lateinit var context: Context
        lateinit var currentDisplayedActivity : AppCompatActivity
    }

}