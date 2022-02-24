package sharedTest.testdata.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Single
import io.reactivex.exceptions.UndeliverableException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class MovieResponseTestData {

    private val movieList = ArrayList<Movie>()

    private fun createMovieList() {
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
        movieList.add(movie1)
        movieList.add(movie2)
    }

    fun getResponseDataSuccess(): Single<Response<MovieResponse>> {
        createMovieList()
        val response: Response<MovieResponse> = Response.success(
            200,
            MovieResponse(
                1,
                movieList,
                10,
                20
            )
        )
        return Single.create { emitter -> emitter.onSuccess(response) }
    }

    fun <T> getResponseDataError(): Single<Response<T>> {
        val response: Response<T> = Response.error(
            "null".toResponseBody("application/json".toMediaTypeOrNull()),

            okhttp3.Response.Builder()
                .code(400)
                .message("Response.error(), bad-request")
                .addHeader("content-type", "application/json")
                .request(Request.Builder().url("http://localhost/").build())
                .protocol(Protocol.HTTP_2)
                .build()
        )
        return Single.create{emitter -> emitter.onSuccess(response)}
    }
}