package com.example.moviedb.core.domain.repository

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.MovieData
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getNowPlayingMovies(): Flow<Resource<List<MovieData>>>
    fun getMovieDetail(id: Int): Flow<Resource<MovieData>>
}