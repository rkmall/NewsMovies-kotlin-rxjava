package com.rupesh.kotlinrxjavaex.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * MovieResponse class represent a HTTP response returned by the server
 * It contains a list of [com.rupesh.kotlinrxjavaex.model.Movie] with additional
 * information i.e. page, total pages and total results
 * @see <a href="https://developers.themoviedb.org/3/getting-started/introduction"></a>
 * @author Rupesh Mall
 * @since 1.0
 */
@Parcelize
data class MovieDBResponse(
    @SerializedName("page")
    @Expose
    val page: Int,

    @SerializedName("results")
    @Expose
    val movies: List<Movie>,

    @SerializedName("total_pages")
    @Expose
    val totalPages: Int,

    @SerializedName("total_results")
    @Expose
    val totalResults: Int
): Parcelable