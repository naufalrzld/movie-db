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
        val updateMovie = MovieEntity(
            2,
            "Movie Title 2 Updated",
            "",
            "Overview of Movie Title 2",
            "2023-01-02",
            "poster_path_2"
        )

        movieLocalDataSource.updateMovie(updateMovie)

        val actual = movieLocalDataSource.getMovieDetail(2).toList()
        val expected = MovieEntity(
            2,
            "Movie Title 2 Updated",
            "",
            "Overview of Movie Title 2",
            "2023-01-02",
            "poster_path_2"
        )

        assertEquals(expected, actual[0])
    }
}