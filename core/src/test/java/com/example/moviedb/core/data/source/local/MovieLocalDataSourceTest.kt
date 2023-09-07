package com.example.moviedb.core.data.source.local

import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(
    GetNowPlayingMoviesLDSTest::class,
    GetDetailMovieLDSTest::class,
    InsertMovieLDSTest::class,
    UpdateMovieLDSTest::class
)
class MovieLocalDataSourceTest