package com.rupesh.kotlinrxjavaex.data.news.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "news")
@Parcelize
data class NewsArticle(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    @ColumnInfo(name = "author")
    @SerializedName("author")
    val author: String?,

    @ColumnInfo(name = "content")
    @SerializedName("content")
    val content: String?,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String?,

    @ColumnInfo(name = "publishedAt")
    @SerializedName("publishedAt")
    val publishedAt: String,

    @ColumnInfo(name = "source")
    @SerializedName("source")
    val source: NewsSource,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String,

    @ColumnInfo(name = "url")
    @SerializedName("url")
    val url: String,

    @ColumnInfo(name = "urlToImage")
    @SerializedName("urlToImage")
    val urlToImage: String?
) : Parcelable
