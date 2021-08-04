package com.beinmedia.test.models.network.control.error.mapper

import com.beinmedia.test.App
import com.beinmedia.test.R
import javax.inject.Inject
import com.beinmedia.test.models.network.control.error.Error

class ErrorMapper @Inject constructor() : ErrorMapperInterface {

    override fun getErrorString(errorId: Int): String {
        return App.context.getString(errorId)
    }

    override val errorsMap: Map<Int, String>
        get() = mapOf(
                Pair(Error.NO_INTERNET_CONNECTION, "No Internet"),
                Pair(Error.NETWORK_ERROR, "Network Error")
        ).withDefault {"Network Error"}
}