package com.example.moviedb.core.data.source.local

import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.local.room.MovieDao
import com.example.moviedb.core.helper.FakeMovieDao
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetNowPlayingMoviesLDSTest {
    private lateinit var fakeMovieDao: MovieDao
    private lateinit var movieLocalDataSource: MovieLocalDataSource

    @Before
    fun setUp() {
        fakeMovieDao = FakeMovieDao()
        movieLocalDataSource = MovieLocalDataSource(fakeMovieDao)
    }

    @Test
    fun `should get now playing movies success`() = runBlocking {
        movieLocalDataSource.insertMovieTvShow(
            listOf(
                MovieEntity(
                    1,
                    "Movie Title 1",
                    "",
                    "Overview of Movie Title 1",
                    "2023-01-01",
                    "poster_path_1"
                ),
                MovieEntity(
                    2,
                    "Movie Title 2",
                    "",
                    "Overview of Movie Title 2",
                    "2023-01-02",
                    "poster_path_2"
                ),
                MovieEntity(
                    3,
                    "Movie Title 3",
                    "",
                    "Overview of Movie Title 3",
                    "2023-01-03",
                    "poster_path_3"
                )
            )
        )
        val actual = movieLocalDataSource.getNowPlayingMovies().toList()
        val expected = listOf(
            MovieEntity(
                1,
                "Movie Title 1",
                "",
                "Overview of Movie Title 1",
                "2023-01-01",
                "poster_path_1"
            ),
            MovieEntity(
                2,
                "Movie Title 2",
                "",
                "Overview of Movie Title 2",
                "2023-01-02",
                "poster_path_2"
            ),
            MovieEntity(
                3,
                "Movie Title 3",
                "",
                "Overview of Movie Title 3",
                "2023-01-03",
                "poster_path_3"
            )
        )

        assertEquals(expected, actual[0])
    }
}