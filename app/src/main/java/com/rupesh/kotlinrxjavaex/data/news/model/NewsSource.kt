package com.rupesh.kotlinrxjavaex.data.news.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsSource(

    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
) : Parcelable