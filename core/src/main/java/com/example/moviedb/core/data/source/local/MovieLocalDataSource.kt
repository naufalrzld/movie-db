package com.example.moviedb.core.data.source.local

import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSource(private val movieDao: MovieDao) {
    fun getNowPlayingMovies(): Flow<List<MovieEntity>> = movieDao.getNowPlayingMovies()

    fun getMovieDetail(id: Int): Flow<MovieEntity> = movieDao.getDetailMovieTvShow(id)

    suspend fun updateMovie(data: MovieEntity) = movieDao.updateMovie(data)

    suspend fun insertMovieTvShow(data: List<MovieEntity>) = movieDao.insertMovie(data)
}