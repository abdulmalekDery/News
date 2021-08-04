package com.beinmedia.test.features.trashTab

import com.beinmedia.test.base.BaseViewModel
import com.beinmedia.test.models.local.models.NewsDB
import com.beinmedia.test.models.manager.DataRepository
import com.beinmedia.test.models.network.control.error.ErrorManager
import com.beinmedia.test.models.network.control.error.mapper.ErrorMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TrashViewModel @Inject constructor(
    val dataRepository: DataRepository,
    override val coroutineContext: CoroutineContext
) : BaseViewModel(), CoroutineScope {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    fun getDeletedNewsFlow(): Flow<List<NewsDB>> {
        return dataRepository.getDeletedNews()
    }

    fun deletePermanently(id: Int){
        launch {
            withContext(Dispatchers.IO)
            {
                dataRepository.deletePermanently(id)
            }
        }
    }
}