package com.rupesh.kotlinrxjavaex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDBResponse(
    @SerializedName("page") val page :                      Int,

    @SerializedName("results") val movies :                List<Movie>,

    @SerializedName("total_pages") val totalPages :        Int,

    @SerializedName("total_results") val totalResults :    Int
): Parcelable