package com.example.moviedb.core.data.source.remote

import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.network.ApiService
import com.example.moviedb.core.data.source.remote.response.GenreResponse
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import com.example.moviedb.core.env.IEnvironment
import com.example.moviedb.core.helper.FakeEnvironment
import com.example.moviedb.core.helper.enqueueResponse
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GetDetailMovieRDSTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var env: IEnvironment
    private lateinit var apiService: ApiService
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        env = FakeEnvironment()
        movieRemoteDataSource = MovieRemoteDataSource(apiService, env)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should get detail movie success given 200 response`() = runBlocking {
        mockWebServer.enqueueResponse("get-detail-movie-200.json", 200)

        val actual = movieRemoteDataSource.getDetailMovie(1).toList()
        val expected = ApiResponse.Success(
            MovieResponse(
                id = 1,
                title = "Meg 2: The Trench",
                genres = listOf(
                    GenreResponse(
                        1,
                        "Action"
                    ),
                    GenreResponse(
                        2,
                        "Science Fiction"
                    ),
                    GenreResponse(
                        3,
                        "Horror"
                    )
                ),
                overview = "Some overview of Meg 2: The Trench",
                releaseDate = "2023-08-02",
                posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
            )
        )

        Truth.assertThat(actual[0]).isInstanceOf(ApiResponse.Success::class.java)
        assertEquals(expected, actual[0])
    }

    @Test
    fun `should get now playing movies failed given 401 response`() = runBlocking {
        mockWebServer.enqueueResponse("get-detail-movie-401.json", 401)

        val actual = movieRemoteDataSource.getDetailMovie(1).toList()
        val expected = ApiResponse.Error(401, "Invalid API key: You must be granted a valid key.")

        Truth.assertThat(actual[0]).isInstanceOf(ApiResponse.Error::class.java)
        assertEquals(expected, actual[0])
    }

    @Test
    fun `should get now playing movies failed given 404 response`() = runBlocking {
        mockWebServer.enqueueResponse("get-detail-movie-404.json", 404)

        val actual = movieRemoteDataSource.getDetailMovie(123).toList()
        val expected = ApiResponse.Error(404, "The resource you requested could not be found.")

        Truth.assertThat(actual[0]).isInstanceOf(ApiResponse.Error::class.java)
        assertEquals(expected, actual[0])
    }
}