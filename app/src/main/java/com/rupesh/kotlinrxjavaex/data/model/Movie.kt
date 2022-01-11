package com.rupesh.kotlinrxjavaex.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Movie class represent a movie data instance
 * @see <a href="https://developers.themoviedb.org/3/getting-started/introduction"></a>
 * @author Rupesh Mall
 * @since 1.0
 */
@Parcelize
data class Movie(
    @SerializedName("adult")
    @Expose
    val adult: Boolean,

    @SerializedName("backdrop_path")
    @Expose
    val backdrop_path: String,

    @SerializedName("genre_ids")
    @Expose
    val genre_ids : List<Int>,

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("original_language")
    @Expose
    val original_language: String,

    @SerializedName("original_title")
    @Expose
    val original_title: String,

    @SerializedName("overview")
    @Expose
    val overview: String,

    @SerializedName("popularity")
    @Expose
    val popularity: Double,

    @SerializedName("poster_path")
    @Expose
    val poster_path: String,

    @SerializedName("release_date")
    @Expose
    val release_date: String,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("video")
    @Expose
    val video: Boolean,

    @SerializedName("vote_average")
    @Expose
    val vote_average: Double,

    @SerializedName("vote_count")
    @Expose
    val vote_count: Int
): Parcelable
