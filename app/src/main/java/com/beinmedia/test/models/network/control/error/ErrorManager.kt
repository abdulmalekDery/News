package com.beinmedia.test.models.network.control.error

import com.beinmedia.test.models.network.control.error.mapper.ErrorFactory
import com.beinmedia.test.models.network.control.error.mapper.ErrorMapper
import javax.inject.Inject

class ErrorManager @Inject constructor(private val errorMapper: ErrorMapper) : ErrorFactory {
    override fun getError(errorCode: Int): Error {
        return Error(code = errorCode, description = errorMapper.errorsMap.getValue(errorCode))
    }

}