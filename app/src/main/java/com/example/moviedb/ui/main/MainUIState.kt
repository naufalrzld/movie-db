package com.example.moviedb.ui.main

import com.example.moviedb.core.domain.model.MovieData

sealed class MainUIState {
    object Loading : MainUIState()
    data class ShowMovies(val movies: List<MovieData>) : MainUIState()
    object Empty : MainUIState()
    data class OnError(val message: String) : MainUIState()
}