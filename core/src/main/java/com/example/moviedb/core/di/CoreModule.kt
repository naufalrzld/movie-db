package com.example.moviedb.core.di

import android.content.Context
import androidx.room.Room
import com.example.moviedb.core.data.repository.MovieRepository
import com.example.moviedb.core.data.source.local.MovieLocalDataSource
import com.example.moviedb.core.data.source.local.room.MovieDao
import com.example.moviedb.core.data.source.local.room.MovieDatabase
import com.example.moviedb.core.data.source.remote.MovieRemoteDataSource
import com.example.moviedb.core.data.source.remote.RetrofitClient
import com.example.moviedb.core.data.source.remote.network.ApiService
import com.example.moviedb.core.domain.repository.IMovieRepository
import com.example.moviedb.core.domain.usecase.MovieInteractor
import com.example.moviedb.core.domain.usecase.MovieUseCase
import com.example.moviedb.core.env.Environment
import com.example.moviedb.core.env.IEnvironment
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single<IEnvironment> { Environment() }
    single { RetrofitClient(get()) }
    single { provideApiService(get()) }
    single { provideRoomDatabase(androidContext()) }
    single { MovieRemoteDataSource(get(), get()) }
    single { MovieLocalDataSource(get()) }
    single<IMovieRepository> { MovieRepository(get(), get()) }
    factory<MovieUseCase> { MovieInteractor(get()) }
}

fun provideRoomDatabase(context: Context): MovieDao {
    val passphrase: ByteArray = SQLiteDatabase.getBytes("example".toCharArray())
    val factory = SupportFactory(passphrase)
    return Room.databaseBuilder(context, MovieDatabase::class.java, "moviedb.db")
        .openHelperFactory(factory)
        .build()
        .movieDao()
}

fun provideApiService(
    retrofitClient: RetrofitClient
): ApiService = retrofitClient.createApiService()
