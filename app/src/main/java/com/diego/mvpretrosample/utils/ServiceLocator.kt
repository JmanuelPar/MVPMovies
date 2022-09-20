package com.diego.mvpretrosample.utils

import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.data.source.remote.MoviesRemoteDataSource
import com.diego.mvpretrosample.network.TmdbApi
import com.diego.mvpretrosample.network.TmdbApiService
import com.diego.mvpretrosample.repository.DefaultMoviesRepository
import com.diego.mvpretrosample.repository.MoviesRepository

object ServiceLocator {

    @Volatile
    var moviesRepository: MoviesRepository? = null

    fun provideMoviesRepository(): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMoviesRepository()
        }
    }

    private fun createMoviesRepository(): MoviesRepository {
        val newRepo = DefaultMoviesRepository(
            moviesRemoteDataSource = createMoviesRemoteDataSource()
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesRemoteDataSource(): MoviesDataSource {
        return MoviesRemoteDataSource(provideApiService())
    }

    private fun provideApiService(): TmdbApiService = TmdbApi.retrofitService
}
