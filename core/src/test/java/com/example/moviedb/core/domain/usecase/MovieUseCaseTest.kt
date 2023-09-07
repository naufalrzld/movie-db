package com.example.moviedb.core.domain.usecase

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(GetNowPlayingMoviesUCTest::class, GetDetailMovieUCTest::class)
class MovieUseCaseTest