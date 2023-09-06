package com.example.moviedb.core.data.source.remote.network

import com.example.moviedb.core.data.source.remote.response.BaseResponseApi
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovie(
        @Query("api_key") apiKey: String
    ): BaseResponseApi<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieID: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse
}