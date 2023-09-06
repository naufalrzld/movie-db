package com.example.moviedb.ui.detail

import com.example.moviedb.core.domain.model.MovieData

sealed class DetailUIState {
    object Loading : DetailUIState()
    data class Loaded(val data: MovieData?) : DetailUIState()
    data class OnError(val message: String) : DetailUIState()
}
