package com.example.moviedb.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.core.data.source.Resource
import com.example.moviedb.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    private val _uiState = MutableSharedFlow<DetailUIState>()
    val uiState: SharedFlow<DetailUIState> = _uiState

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            movieUseCase.getMovieDetail(id).collect { resource ->
                val uiState = when (resource) {
                    is Resource.Loading -> DetailUIState.Loading
                    is Resource.Success -> DetailUIState.Loaded(resource.data)
                    is Resource.Error -> DetailUIState.OnError(resource.message ?: "Something went wrong")
                }

                _uiState.emit(uiState)
            }
        }
    }
}