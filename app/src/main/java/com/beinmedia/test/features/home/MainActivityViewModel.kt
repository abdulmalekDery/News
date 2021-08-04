package com.beinmedia.test.features.home

import androidx.lifecycle.MutableLiveData
import com.beinmedia.test.base.BaseViewModel
import com.beinmedia.test.models.manager.DataRepository
import com.beinmedia.test.models.network.control.Resource
import com.beinmedia.test.models.network.control.error.Error
import com.beinmedia.test.models.network.control.error.ErrorManager
import com.beinmedia.test.models.network.control.error.mapper.ErrorMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel @Inject constructor(
    val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext
) : BaseViewModel(), CoroutineScope {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())



}