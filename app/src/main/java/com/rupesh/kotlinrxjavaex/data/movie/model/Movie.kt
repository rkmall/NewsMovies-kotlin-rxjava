package com.rupesh.kotlinrxjavaex.data.movie.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Movie class represent a movie data instance
 * @see <a href="https://developers.themoviedb.org/3/getting-started/introduction"></a>
 * @author Rupesh Mall
 * @since 1.0
 */
@Entity(tableName = "movies")
@Parcelize
data class Movie(

    @SerializedName("itemId")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("genre_ids")
    val genreIds : List<Int>,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Int
): Parcelable
