package com.beinmedia.test.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import com.beinmedia.test.App
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject


open class BaseActivity: DaggerAppCompatActivity() {

    companion object {
        public var dLocale: Locale? = null
    }

    init {
        updateConfig(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
    }
    fun getCurrentLocale(context: Context): Locale? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            App.context.resources.configuration.locales[0]
        } else {
            App.context.resources.configuration.locale
        }
    }
}