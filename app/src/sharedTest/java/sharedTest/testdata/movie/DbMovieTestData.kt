package sharedTest.testdata.movie

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import io.reactivex.Observable

class DbMovieTestData {

    private val movieList: ArrayList<DbMovie> = ArrayList()

    private fun createMovieList() {

        val movie1 = DbMovie(
            1L,
            "Movie-1",
            7.0,
            "The movie of the year",
            "20/01/2020",
            "https://the_movie_poster123.com",
        )

        val movie2 = DbMovie(
            2L,
            "Movie-2",
            7.0,
            "The movie after the event",
            "20/01/2021",
            "https://the_movie_poster234.com",
        )
        movieList.add(movie1)
        movieList.add(movie2)
    }

    fun getMovieListTestData(): List<DbMovie> {
        createMovieList()
        return movieList
    }

    fun getMovieListTestDataObservable(): Observable<List<DbMovie>> {
        createMovieList()
        return Observable.create { emitter -> emitter.onNext(movieList)}
    }
}