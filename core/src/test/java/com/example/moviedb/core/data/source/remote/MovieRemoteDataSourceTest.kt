package com.example.moviedb.core.data.source.remote

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(GetNowPlayingMoviesRDSTest::class, GetDetailMovieRDSTest::class)
class MovieRemoteDataSourceTest