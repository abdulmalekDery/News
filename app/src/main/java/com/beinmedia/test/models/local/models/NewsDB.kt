package com.beinmedia.test.models.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsDB (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "description") val description : String,
    @ColumnInfo(name = "content") val content : String,
    @ColumnInfo(name = "url") val url : String,
    @ColumnInfo(name = "image") val image : String,
    @ColumnInfo(name = "publishedAt") val publishedAt : String,
    @ColumnInfo(name = "isDeleted") var isDeleted: Boolean,
    @ColumnInfo(name = "deleteDate") var deleteDate: String,
)