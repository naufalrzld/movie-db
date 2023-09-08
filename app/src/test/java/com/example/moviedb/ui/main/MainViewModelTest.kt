package com.example.moviedb.ui.main

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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class MainViewModelTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    private lateinit var movieUseCase: MovieUseCase
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        movieUseCase = mock(MovieInteractor::class.java)
        mainViewModel = MainViewModel(movieUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `should success get now playing movies`() = runTest {
        `when`(movieUseCase.getNowPlayingMovies()).thenReturn(
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

        val expected = MainUIState.ShowMovies(
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

        mainViewModel.getNowPlayingMovies()

        mainViewModel.uiState.test {
            assertEquals(MainUIState.Loading, awaitItem())
            assertEquals(expected, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        verify(movieUseCase, times(1)).getNowPlayingMovies()
    }

    @Test
    fun `should success get now playing movies with empty value`() = runTest {
        `when`(movieUseCase.getNowPlayingMovies()).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Success(listOf()))
            }
        )

        val expected = MainUIState.Empty

        mainViewModel.getNowPlayingMovies()

        mainViewModel.uiState.test {
            assertEquals(MainUIState.Loading, awaitItem())
            assertEquals(expected, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        verify(movieUseCase, times(1)).getNowPlayingMovies()
    }

    @Test
    fun `should error get now playing movies`() = runTest {
        `when`(movieUseCase.getNowPlayingMovies()).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Error(401, "auth error"))
            }
        )

        val expected = MainUIState.OnError("auth error")

        mainViewModel.getNowPlayingMovies()

        mainViewModel.uiState.test {
            assertEquals(MainUIState.Loading, awaitItem())
            assertEquals(expected, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        verify(movieUseCase, times(1)).getNowPlayingMovies()
    }
}