package com.beinmedia.test.models.network.control.error.mapper

interface ErrorMapperInterface {
    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
}