package com.example.moviedb.core.data.repository

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.data.source.local.MovieLocalDataSource
import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.local.room.MovieDao
import com.example.moviedb.core.data.source.remote.MovieRemoteDataSource
import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.response.GenreResponse
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.helper.FakeMovieDao
import com.example.moviedb.core.utils.DataMapper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class GetDetailMovieRepoTest {
    private lateinit var movieRepository: IMovieRepository
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource
    private lateinit var movieLocalDataSource: MovieLocalDataSource
    private lateinit var fakeMovieDao: MovieDao

    @Before
    fun setUp() {
        movieRemoteDataSource = mock(MovieRemoteDataSource::class.java)
        fakeMovieDao = FakeMovieDao()
        movieLocalDataSource = spy(MovieLocalDataSource(fakeMovieDao))
        movieRepository = MovieRepository(movieRemoteDataSource, movieLocalDataSource)
    }

    @Test
    fun `should success get detail movie and update genre to local data source`() = runTest {
        movieLocalDataSource.insertMovieTvShow(
            listOf(
                MovieEntity(
                    1,
                    "Meg 2: The Trench",
                    "",
                    "Some overview of Meg 2: The Trench",
                    "2023-08-02",
                    "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
                )
            )
        )

        `when`(movieRemoteDataSource.getDetailMovie(1)).thenReturn(
            flow {
                emit(
                    ApiResponse.Success(
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
                )
            }
        )

        val actual = movieRepository.getDetailMovie(1).toList()
        val expected = MovieData(
            id = 1,
            title = "Meg 2: The Trench",
            genres = "Action - Science Fiction - Horror",
            overview = "Some overview of Meg 2: The Trench",
            releaseDate = "2023-08-02",
            posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
        )

        assertThat(actual[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(actual[1]).isInstanceOf(Resource.Loading::class.java)
        assertThat(actual[2]).isInstanceOf(Resource.Success::class.java)
        assertEquals(expected, actual[2].data)

        verify(movieRemoteDataSource, times(1)).getDetailMovie(1)
        verify(movieLocalDataSource, times(2)).getMovieDetail(1)
        verify(movieLocalDataSource, times(1)).updateMovie(
            DataMapper.mapMovieResponseToEntity(
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
        )
    }
}