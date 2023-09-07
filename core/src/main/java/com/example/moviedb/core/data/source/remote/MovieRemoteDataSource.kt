package com.example.moviedb.core.data.source.remote

import com.example.moviedb.core.data.source.remote.network.ApiResponse
import com.example.moviedb.core.data.source.remote.network.ApiService
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import com.example.moviedb.core.env.IEnvironment
import com.example.moviedb.core.utils.ResponseErrorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRemoteDataSource(
    private val apiService: ApiService,
    private val env: IEnvironment
) {
    fun getNowPlayingMovies(): Flow<ApiResponse<List<MovieResponse>>> = flow {
        try {
            val response = apiService.getNowPlayingMovie(env.getAPIKey())
            val movies = response.results
            if (movies.isNotEmpty()) emit(ApiResponse.Success(movies))
            else emit(ApiResponse.Empty)
        } catch (e: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(e)
            emit(ApiResponse.Error(code, message))
        }
    }.flowOn(Dispatchers.IO)

    fun getDetailMovie(id: Int): Flow<ApiResponse<MovieResponse>> = flow {
        try {
            val response = apiService.getDetailMovie(id, env.getAPIKey())
            emit(ApiResponse.Success(response))
        } catch (e: Throwable) {
            val (code, message) = ResponseErrorMapper.mapResponseError(e)
            emit(ApiResponse.Error(code, message))
        }
    }.flowOn(Dispatchers.IO)
}