package com.rupesh.kotlinrxjavaex.data.movie.db

import androidx.room.*
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * MovieDao is an interface that represents Data Access Object and
 * interacts with underlying SQLite database using Room database to perform
 * database operations for entity [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
 * @author Rupesh Mall
 * @since 1.0
 */
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movie: DbMovie): Maybe<Long>

    @Query("delete from movies where id = :id")
    fun deleteMovie(id: Int): Maybe<Int>

    @Query("select * from movies")
    fun getAllMovie(): Observable<List<DbMovie>>
}