package com.beinmedia.test.models.network.control

import com.beinmedia.test.models.network.control.error.Errors

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val error: Any? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class DataError<T>(errorCode: Int) : Resource<T>(null, errorCode)
    class Exception<T>(errorMessage: String) : Resource<T>(null, errorMessage)
    class ValidationError<T>(errorMessage: Errors) : Resource<T>(null, errorMessage)
}