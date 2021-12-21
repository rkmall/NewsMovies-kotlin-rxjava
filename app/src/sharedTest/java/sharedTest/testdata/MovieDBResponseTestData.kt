package sharedTest.testdata

import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.model.MovieDBResponse
import io.reactivex.Observable

class MovieDBResponseTestData {

    val movieList = ArrayList<Movie>()

    fun getTestObservable(): Observable<MovieDBResponse> {
        val movie1 = Movie(
            false,
            "backdrop1",
            List<Int>(1){0},
            0,
            "English",
            "Movie2020",
            "Movie of the year",
            8.0,
            "https://the_movie_poster123.com",
            "20/01/2021",
            "Movie of the year",
            true,
            8.0,
            100
        )
        movieList.add(movie1)

        val movie2 = Movie(
            false,
            "backdrop2",
            List<Int>(1){0},
            0,
            "English",
            "Movie2021",
            "Movie after the event",
            9.0,
            "https://the_movie_poster234.com",
            "20/01/2022",
            "Movie after the event",
            true,
            9.0,
            150
        )
        movieList.add(movie2)

        val movieDBResponse: MovieDBResponse = MovieDBResponse(
            10,
            movieList,
            10,
            20
        )

        return Observable.just(movieDBResponse)
    }
}