package com.beinmedia.test.base

import com.beinmedia.test.models.network.control.error.Error

interface BaseCallback {
    fun onSuccess(data: Any)

    fun onFail(error : Error)
}
