package com.beinmedia.test.models.network.control.error.mapper

import com.beinmedia.test.models.network.control.error.Error

interface ErrorFactory {
    fun getError(errorCode: Int): Error
}