package com.rupesh.kotlinrxjavaex.data.news.model

import com.google.gson.annotations.SerializedName

data class NewsSource(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)