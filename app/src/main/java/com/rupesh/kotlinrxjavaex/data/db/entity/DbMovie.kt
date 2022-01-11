package com.rupesh.kotlinrxjavaex.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * DbMovie class represent Movie entity that is used to store movie
 * information in the persistent local storage
 * @author Rupesh Mall
 * @since 1.0
 */
@Entity(tableName = "movies")
data class DbMovie(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "rating")
    val rating: Double,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "release_date")
    val releaseDate: String,

    @ColumnInfo(name = "poster_path")
    val posterPath: String
)
