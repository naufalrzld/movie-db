package com.example.moviedb.core.domain.usecase

import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override fun getNowPlayingMovies(): Flow<Resource<List<MovieData>>> = movieRepository.getNowPlayingMovies()
    override fun getMovieDetail(id: Int): Flow<Resource<MovieData>> = movieRepository.getMovieDetail(id)
}