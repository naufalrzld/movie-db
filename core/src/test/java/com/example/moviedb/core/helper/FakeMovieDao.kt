package com.example.moviedb.core.helper

import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMovieDao : MovieDao {
    private val movies = arrayListOf(
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

    override fun getNowPlayingMovies(): Flow<List<MovieEntity>> = flow {
        emit(movies)
    }

    override fun getDetailMovie(id: Int): Flow<MovieEntity> = flow {
        movies.find { it.id == id }?.let {
            emit(it)
        }
    }

    override suspend fun insertMovie(data: List<MovieEntity>) {
        movies.addAll(data)
    }

    override suspend fun updateMovie(data: MovieEntity) {
        val movie = movies.find { it.id == data.id }
        if (movie != null) {
            val index = movies.indexOf(movie)
            movies[index] = data
        }
    }
}