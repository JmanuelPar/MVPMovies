package com.diego.mvpretrosample.utils

import android.content.Context
import com.diego.mvpretrosample.data.source.remote.MoviesRemoteDataSource
import com.diego.mvpretrosample.db.MovieRoomDatabase
import com.diego.mvpretrosample.network.TmdbApi
import com.diego.mvpretrosample.repository.DefaultMoviesRepository
import com.diego.mvpretrosample.repository.MoviesRepository

object ServiceLocator {

    @Volatile
    var moviesRepository: MoviesRepository? = null

    fun provideMoviesRepository(context: Context): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMoviesRepository(context)
        }
    }

    private fun createMoviesRepository(context: Context): MoviesRepository {
        val newRepo = DefaultMoviesRepository(
            moviesRemoteDataSource = createMoviesRemoteDataSource(context)
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesRemoteDataSource(context: Context) =
        MoviesRemoteDataSource(
            apiService = provideApiService(),
            movieRoomDatabase = provideMovieDatabase(context)
        )

    private fun provideApiService() = TmdbApi.retrofitService

    private fun provideMovieDatabase(context: Context) = MovieRoomDatabase.getInstance(context)
}
