package com.example.moviedb.core.data.source.local

import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.local.room.MovieDao
import com.example.moviedb.core.helper.FakeMovieDao
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InsertMovieLDSTest {
    private lateinit var fakeMovieDao: MovieDao
    private lateinit var movieLocalDataSource: MovieLocalDataSource

    @Before
    fun setUp() {
        fakeMovieDao = FakeMovieDao()
        movieLocalDataSource = MovieLocalDataSource(fakeMovieDao)
    }

    @Test
    fun `should insert movie success`() = runBlocking {
        val newMovie = listOf(
            MovieEntity(
                4,
                "Movie Title 4",
                "",
                "Overview of Movie Title 4",
                "2023-01-04",
                "poster_path_4"
            )
        )

        movieLocalDataSource.insertMovieTvShow(newMovie)

        val actual = movieLocalDataSource.getMovieDetail(4).toList()
        val expected = MovieEntity(
            4,
            "Movie Title 4",
            "",
            "Overview of Movie Title 4",
            "2023-01-04",
            "poster_path_4"
        )

        assertEquals(expected, actual[0])
    }
}