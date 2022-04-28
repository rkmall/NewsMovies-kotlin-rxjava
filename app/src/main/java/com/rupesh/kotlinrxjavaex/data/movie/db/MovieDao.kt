package com.rupesh.kotlinrxjavaex.data.movie.db

import androidx.room.*
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import io.reactivex.Maybe
import io.reactivex.Observable

/**
 * MovieDao is an interface that represents Data Access Object and
 * interacts with underlying SQLite database using Room database to perform
 * database operations for entity [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
 * @author Rupesh Mall
 * @since 1.0
 */
@Dao
interface MovieDao {

    @Query("select * from movies")
    fun getSavedMovies(): Observable<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie): Maybe<Long>

    @Query("delete from movies where itemId = :itemId")
    fun deleteMovie(itemId: Int): Maybe<Int>

    @Query("delete from movies")
    fun clear(): Maybe<Int>
}