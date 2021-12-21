package com.rupesh.kotlinrxjavaex.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rupesh.kotlinrxjavaex.db.entity.DbMovie
import io.reactivex.Observable
import io.reactivex.Single

/**
 * MovieDao is an interface that represents Data Access Object and
 * interacts with local SQLite database using room database to perform
 * database operations for entity [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
 * @author Rupesh Mall
 * @since 1.0
 */
@Dao
interface MovieDao {

    @Insert
    fun addMovie(movie: DbMovie): Long

    @Delete
    fun deleteMovie(movie: DbMovie): Int

    @Query("select * from movies")
    fun getAllMovie(): Observable<List<DbMovie>>

    @Query("select * from movies where id = :id")
    fun getMovie(id: Long): Single<DbMovie>
}