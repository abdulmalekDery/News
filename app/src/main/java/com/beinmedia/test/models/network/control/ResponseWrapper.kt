package com.beinmedia.test.models.network.control

import com.beinmedia.test.models.network.control.error.Errors

data class ResponseWrapper<T>(val message:String, val data: T, val errors: Errors)