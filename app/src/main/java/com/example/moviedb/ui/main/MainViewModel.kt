package com.example.moviedb.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val _uiState = MutableSharedFlow<MainUIState>()
    val uiState: SharedFlow<MainUIState> = _uiState

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            movieUseCase.getNowPlayingMovies().collect { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> MainUIState.Loading
                    is Resource.Success -> {
                        val movies = resource.data
                        if (movies.isNullOrEmpty()) MainUIState.Empty
                        else MainUIState.ShowMovies(movies)
                    }
                    is Resource.Error -> MainUIState.OnError(
                        resource.message ?: "Something went wrong"
                    )
                }
                _uiState.emit(uiState)
            }
        }
    }
}