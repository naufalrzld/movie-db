package com.example.moviedb.core.utils

import com.example.moviedb.core.data.source.local.entity.MovieEntity
import com.example.moviedb.core.data.source.remote.response.MovieResponse
import com.example.moviedb.core.domain.model.MovieData

object DataMapper {
    fun mapMovieResponseToEntity(input: List<MovieResponse>): List<MovieEntity> =
        input.map {
            MovieEntity(
                id = it.id,
                title = it.title,
                genres = "",
                overview = it.overview,
                releaseDate = it.releaseDate ?: "-",
                posterPath = it.posterPath ?: "",
                favorited = false
            )
        }

    fun mapMovieEntityToDomain(input: List<MovieEntity>): List<MovieData> =
        input.map {
            MovieData(
                id = it.id,
                title = it.title,
                genres = it.genres,
                overview = it.overview,
                releaseDate = it.releaseDate,
                posterPath = it.posterPath,
                favorited = it.favorited
            )
        }

    fun mapMovieResponseToEntity(input: MovieResponse): MovieEntity {
        val genres = ArrayList<String>()
        input.genres?.forEach { genres.add(it.name) }
        val genre = genres.joinToString(" - ")

        return MovieEntity(
            id = input.id,
            title = input.title,
            genres = genre,
            overview = input.overview,
            releaseDate = input.releaseDate ?: "-",
            posterPath = input.posterPath ?: "",
            favorited = false
        )
    }

    fun mapMovieEntityToDomain(input: MovieEntity): MovieData =
        MovieData(
            id = input.id,
            title = input.title,
            genres = input.genres,
            overview = input.overview,
            releaseDate = input.releaseDate,
            posterPath = input.posterPath,
            favorited = input.favorited
        )
}