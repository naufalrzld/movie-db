package com.example.moviedb.core.data.source.local

import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.local.room.MovieDao
import com.example.moviedb.core.helper.FakeMovieDao
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateMovieLDSTest {
    private lateinit var fakeMovieDao: MovieDao
    private lateinit var movieLocalDataSource: MovieLocalDataSource

    @Before
    fun setUp() {
        fakeMovieDao = FakeMovieDao()
        movieLocalDataSource = MovieLocalDataSource(fakeMovieDao)
    }

    @Test
    fun `should insert movie success`() = runBlocking {
        movieLocalDataSource.insertMovieTvShow(
            listOf(
                MovieEntity(
                    1,
                    "Movie Title 1",
                    "",
                    "Overview of Movie Title 1",
                    "2023-01-01",
                    "poster_path_1"
                )
            )
        )

        val updateMovie = MovieEntity(
            1,
            "Movie Title 1 Updated",
            "",
            "Overview of Movie Title 1",
            "2023-01-01",
            "poster_path_1"
        )

        movieLocalDataSource.updateMovie(updateMovie)

        val actual = movieLocalDataSource.getMovieDetail(1).toList()
        val expected = MovieEntity(
            1,
            "Movie Title 1 Updated",
            "",
            "Overview of Movie Title 1",
            "2023-01-01",
            "poster_path_1"
        )

        assertEquals(expected, actual[0])
    }
}