package com.example.moviedb.di

import com.example.moviedb.ui.detail.DetailViewModel
import com.example.moviedb.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}