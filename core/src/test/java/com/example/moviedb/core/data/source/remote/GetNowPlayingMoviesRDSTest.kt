package com.example.moviedb.core.data.source.remote

import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.network.ApiService
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import com.example.moviedb.core.env.IEnvironment
import com.example.moviedb.core.helper.FakeEnvironment
import com.example.moviedb.core.helper.enqueueResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class GetNowPlayingMoviesRDSTest {
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

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = spy(retrofit.create(ApiService::class.java))

        env = FakeEnvironment()
        movieRemoteDataSource = MovieRemoteDataSource(apiService, env)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should get now playing movies success given 200 response`() = runTest {
        mockWebServer.enqueueResponse("get-now-playing-movie-200.json", 200)

        val actual = movieRemoteDataSource.getNowPlayingMovies().toList()
        val expected = ApiResponse.Success(
            listOf(
                MovieResponse(
                    id = 1,
                    title = "Meg 2: The Trench",
                    genres = null,
                    overview = "Some overview of Meg 2: The Trench",
                    releaseDate = "2023-08-02",
                    posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
                ),
                MovieResponse(
                    id = 2,
                    title = "Barbie",
                    genres = null,
                    overview = "Some overview of Barbie",
                    releaseDate = "2023-07-19",
                    posterPath = "/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg"
                ),
                MovieResponse(
                    id = 3,
                    title = "Elemental",
                    genres = null,
                    overview = "Some overview of Elemental",
                    releaseDate = "2023-06-14",
                    posterPath = "/4Y1WNkd88JXmGfhtWR7dmDAo1T2.jpg"
                )
            )
        )

        assertThat(actual[0]).isInstanceOf(ApiResponse.Success::class.java)
        assertEquals(expected, actual[0])
        verify(apiService, times(1)).getNowPlayingMovie(env.getAPIKey())
    }

    @Test
    fun `should get now playing movies success given 200 response with empty movie`() = runTest {
        mockWebServer.enqueueResponse("get-now-playing-movie-200-empty.json", 200)

        val actual = movieRemoteDataSource.getNowPlayingMovies().toList()
        val expected = ApiResponse.Empty

        assertThat(actual[0]).isInstanceOf(ApiResponse.Empty::class.java)
        assertEquals(expected, actual[0])
        verify(apiService, times(1)).getNowPlayingMovie(env.getAPIKey())
    }

    @Test
    fun `should get now playing movies failed given 401 response`() = runTest {
        mockWebServer.enqueueResponse("get-now-playing-movie-401.json", 401)

        val actual = movieRemoteDataSource.getNowPlayingMovies().toList()
        val expected = ApiResponse.Error(401, "Invalid API key: You must be granted a valid key.")

        assertThat(actual[0]).isInstanceOf(ApiResponse.Error::class.java)
        assertEquals(expected, actual[0])
        verify(apiService, times(1)).getNowPlayingMovie(env.getAPIKey())
    }
}