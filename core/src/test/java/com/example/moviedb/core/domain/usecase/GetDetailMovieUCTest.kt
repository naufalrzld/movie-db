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
class GetDetailMovieUCTest {
    private lateinit var movieUseCase: MovieUseCase
    private lateinit var movieRepository: IMovieRepository

    @Before
    fun setUp() {
        movieRepository = mock(MovieRepository::class.java)
        movieUseCase = MovieInteractor(movieRepository)
    }

    @Test
    fun `should get detail movie correctly`() = runTest {
        `when`(movieRepository.getDetailMovie(1)).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(
                    Resource.Success(
                        MovieData(
                            id = 1,
                            title = "Meg 2: The Trench",
                            genres = "",
                            overview = "Some overview of Meg 2: The Trench",
                            releaseDate = "2023-08-02",
                            posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
                        )
                    )
                )
            }
        )

        val actual = movieUseCase.getDetailMovie(1).toList()
        val expected = MovieData(
            id = 1,
            title = "Meg 2: The Trench",
            genres = "",
            overview = "Some overview of Meg 2: The Trench",
            releaseDate = "2023-08-02",
            posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
        )

        assertThat(actual[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(actual[1]).isInstanceOf(Resource.Success::class.java)
        assertEquals(expected, (actual[1] as Resource.Success).data)
        verify(movieRepository, Mockito.times(1)).getDetailMovie(1)
    }
}