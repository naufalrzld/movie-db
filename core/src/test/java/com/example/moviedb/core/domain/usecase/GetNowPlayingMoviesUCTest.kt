package com.example.moviedb.core.domain.usecase

import com.example.moviedb.core.data.repository.MovieRepository
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class GetNowPlayingMoviesUCTest {
    private lateinit var movieUseCase: MovieUseCase
    private lateinit var movieRepository: IMovieRepository

    @Before
    fun setUp() {
        movieRepository = mock(MovieRepository::class.java)
        movieUseCase = MovieInteractor(movieRepository)
    }

    @Test
    fun `should get now playing movies correctly`() = runTest {
        `when`(movieRepository.getNowPlayingMovies()).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(
                    Resource.Success(
                        listOf(
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
                    )
                )
            }
        )

        val actual = movieUseCase.getNowPlayingMovies().toList()
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
        assertThat(actual[1]).isInstanceOf(Resource.Success::class.java)
        assertEquals(expected, (actual[1] as Resource.Success).data)
        verify(movieRepository, Mockito.times(1)).getNowPlayingMovies()
    }
}