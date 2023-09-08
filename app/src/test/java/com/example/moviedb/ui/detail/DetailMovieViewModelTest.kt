package com.example.moviedb.ui.detail

import app.cash.turbine.test
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.usecase.MovieInteractor
import com.example.moviedb.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class DetailMovieViewModelTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    private lateinit var movieUseCase: MovieUseCase
    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        movieUseCase = Mockito.mock(MovieInteractor::class.java)
        detailViewModel = DetailViewModel(movieUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `should success get detail movie`() = runTest {
        `when`(movieUseCase.getDetailMovie(1)).thenReturn(
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

        val expected = DetailUIState.Loaded(
            MovieData(
                id = 1,
                title = "Meg 2: The Trench",
                genres = "",
                overview = "Some overview of Meg 2: The Trench",
                releaseDate = "2023-08-02",
                posterPath = "/FQHtuf2zc8suMFE28RyvFt3FJN.jpg"
            )
        )

        detailViewModel.getDetailMovie(1)

        detailViewModel.uiState.test {
            Assert.assertEquals(DetailUIState.Loading, awaitItem())
            Assert.assertEquals(expected, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        verify(movieUseCase, times(1)).getDetailMovie(1)
    }

    @Test
    fun `should error get detail movie`() = runTest {
        `when`(movieUseCase.getDetailMovie(1)).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Error(404, "not found"))
            }
        )

        val expected = DetailUIState.OnError("not found")

        detailViewModel.getDetailMovie(1)

        detailViewModel.uiState.test {
            Assert.assertEquals(DetailUIState.Loading, awaitItem())
            Assert.assertEquals(expected, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        verify(movieUseCase, times(1)).getDetailMovie(1)
    }
}