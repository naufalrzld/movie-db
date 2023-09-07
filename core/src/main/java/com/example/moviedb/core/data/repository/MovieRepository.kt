package com.example.moviedb.core.data.repository

import com.example.moviedb.core.data.NetworkBoundResource
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.data.source.local.MovieLocalDataSource
import com.example.moviedb.core.data.source.remote.MovieRemoteDataSource
import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import com.example.moviedb.core.domain.model.MovieData
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource
) : IMovieRepository {
    override fun getNowPlayingMovies(): Flow<Resource<List<MovieData>>> {
        return object : NetworkBoundResource<List<MovieData>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<MovieData>> {
                return movieLocalDataSource.getNowPlayingMovies().map {
                    DataMapper.mapMovieEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return movieRemoteDataSource.getNowPlayingMovies()
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                movieLocalDataSource.insertMovieTvShow(
                    DataMapper.mapMovieResponseToEntity(data)
                )
            }

            override fun shouldFetch(data: List<MovieData>?): Boolean = data.isNullOrEmpty()

        }.asFlow()
    }

    override fun getDetailMovie(id: Int): Flow<Resource<MovieData>> {
        return object : NetworkBoundResource<MovieData, MovieResponse>() {
            override fun loadFromDB(): Flow<MovieData> {
                return movieLocalDataSource.getMovieDetail(id).map {
                    DataMapper.mapMovieEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<MovieResponse>> {
                return movieRemoteDataSource.getDetailMovie(id)
            }

            override suspend fun saveCallResult(data: MovieResponse) {
                movieLocalDataSource.updateMovie(DataMapper.mapMovieResponseToEntity(data))
            }

            override fun shouldFetch(data: MovieData?): Boolean {
                return data?.genres.isNullOrEmpty()
            }

        }.asFlow()
    }
}