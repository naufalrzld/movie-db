package com.example.moviedb.core.data.repository

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(GetNowPlayingMoviesRepoTest::class, GetDetailMovieRepoTest::class)
class MovieRepositoryTest