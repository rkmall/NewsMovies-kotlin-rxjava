package sharedTest.testdata

import com.rupesh.kotlinrxjavaex.db.entity.DbMovie

class DbMovieTestData {

    val movieList: ArrayList<DbMovie> = ArrayList()

    fun getTestMovieList(): List<DbMovie> {

        val movie1 = DbMovie(
            0L,
            "Movie2020",
            7.0,
            "20/01/2020",
            "The movie of the year",
            "https://the_movie_poster123.com",
        )
        movieList.add(movie1)

        val movie2 = DbMovie(
            0L,
            "Movie2021",
            7.0,
            "20/01/2021",
            "The movie after the event",
            "https://the_movie_poster234.com",
        )
        movieList.add(movie2)

        return movieList
    }
}