package com.example.moviedb.core.data.repository

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.data.source.local.MovieLocalDataSource
import com.example.moviedb.core.data.source.local.room.MovieDao
import com.example.moviedb.core.data.source.remote.MovieRemoteDataSource
import com.example.moviedb.core.data.source.remote.network.ApiResponse
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
class GetNowPlayingMoviesRepoTest {
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
    fun `should success get now playing movies and store to local data source`() = runTest {
        `when`(movieRemoteDataSource.getNowPlayingMovies()).thenReturn(
            flow {
                emit(
                    ApiResponse.Success(
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
                )
            }
        )

        val actual = movieRepository.getNowPlayingMovies().toList()
        val expected = listOf(
            MovieData(
                id = 1,
                title = "Meg 2: The Trench",
                genres = "",
                overview = "Some overview of Meg 2: The Trench",
                releaseDate = "2023-08-02",
                posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
            ),
            MovieData(
                id = 2,
                title = "Barbie",
                genres = "",
                overview = "Some overview of Barbie",
                releaseDate = "2023-07-19",
                posterPath = "/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg"
            ),
            MovieData(
                id = 3,
                title = "Elemental",
                genres = "",
                overview = "Some overview of Elemental",
                releaseDate = "2023-06-14",
                posterPath = "/4Y1WNkd88JXmGfhtWR7dmDAo1T2.jpg"
            )
        )

        assertThat(actual[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(actual[1]).isInstanceOf(Resource.Loading::class.java)
        assertThat(actual[2]).isInstanceOf(Resource.Success::class.java)
        assertEquals(expected, actual[2].data)

        verify(movieRemoteDataSource, times(1)).getNowPlayingMovies()
        verify(movieLocalDataSource, times(2)).getNowPlayingMovies()
        verify(movieLocalDataSource, times(1)).insertMovieTvShow(
            DataMapper.mapMovieResponseToEntity(
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
        )
    }
}