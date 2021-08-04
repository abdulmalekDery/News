package com.beinmedia.test.models.network.pojo.home

import com.google.gson.annotations.SerializedName

data class News (

    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("content") var content : String,
    @SerializedName("url") var url : String,
    @SerializedName("image") val image : String,
    @SerializedName("publishedAt") val publishedAt : String,

)