package com.diego.mvpretrosample.utils

import android.content.Context
import com.diego.mvpretrosample.data.source.remoteMediator.MoviesRemoteMediatorDataSource
import com.diego.mvpretrosample.db.MoviesRoomDatabase
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
            moviesRemoteMediatorDataSource = createMoviesRemoteMediatorDataSource(context)
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesRemoteMediatorDataSource(context: Context) =
        MoviesRemoteMediatorDataSource(
            apiService = provideApiService(),
            moviesRoomDatabase = provideMoviesDatabase(context)
        )

    private fun provideApiService() = TmdbApi.retrofitService

    private fun provideMoviesDatabase(context: Context) = MoviesRoomDatabase.getInstance(context)
}
