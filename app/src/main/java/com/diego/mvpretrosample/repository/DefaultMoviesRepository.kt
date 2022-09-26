package com.diego.mvpretrosample.repository

import com.diego.mvpretrosample.data.source.MoviesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultMoviesRepository(
    private val moviesRemoteDataSource: MoviesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesRepository {

    override fun getMovies() = moviesRemoteDataSource.getMovies()

    override suspend fun getMovieById(movieId: Int) = withContext(ioDispatcher) {
        moviesRemoteDataSource.getMovieById(movieId)
    }
}