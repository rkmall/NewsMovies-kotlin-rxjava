package com.rupesh.kotlinrxjavaex.data.movie.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * MovieResponse class represent a HTTP response returned by the server
 * It contains a list of [com.rupesh.kotlinrxjavaex.model.Movie] with additional
 * information i.e. page, total pages and total results
 * @see <a href="https://developers.themoviedb.org/3/getting-started/introduction"></a>
 * @author Rupesh Mall
 * @since 1.0
 */
@Parcelize
data class MovieResponse(

    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val movies: List<Movie>,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int
): Parcelable