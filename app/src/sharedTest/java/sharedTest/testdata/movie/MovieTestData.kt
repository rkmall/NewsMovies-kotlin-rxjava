package sharedTest.testdata.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class MovieTestData {

    private val mMovieList = ArrayList<Movie>()
    val movieList get() = mMovieList

    init { createMovieList() }

    private fun createMovieList() {
        val movie1 = Movie(
            1,
            false,
            "backdrop1",
            List(1){0},
            "English",
            "First Movie",
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
            2,
            false,
            "backdrop2",
            List(1){0},
            "English",
            "Second Movie",
            "Movie after the event",
            9.0,
            "https://the_movie_poster234.com",
            "20/01/2022",
            "Movie after the event",
            true,
            9.0,
            150
        )
        mMovieList.add(movie1)
        mMovieList.add(movie2)
    }


    fun createSuccessResponseMovieObservable(): Single<Response<MovieResponse>> {
        val response: Response<MovieResponse> = Response.success(
            200,
            MovieResponse(
                page = 1,
                movies =  mMovieList,
                totalPages = 10,
                totalResults = 20
            )
        )
        return Single.create { emitter -> emitter.onSuccess(response) }
    }


    fun createErrorResponseMovieObservable(): Single<Response<MovieResponse>> {
        val response: Response<MovieResponse> = Response.error(
            "null".toResponseBody("application/json".toMediaTypeOrNull()),

            okhttp3.Response.Builder()
                .code(400)
                .message("bad-request")
                .addHeader("content-type", "application/json")
                .request(Request.Builder().url("http://localhost/").build())
                .protocol(Protocol.HTTP_1_1)
                .build()
        )
        return Single.create{emitter -> emitter.onSuccess(response)}
    }
}