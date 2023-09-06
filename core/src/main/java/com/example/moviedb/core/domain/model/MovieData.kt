package com.example.moviedb.core.domain.model

data class MovieData(
    val id: Int,
    val title: String,
    val genres: String,
    val releaseDate: String,
    val overview: String,
    val posterPath: String,
    val favorited: Boolean
)