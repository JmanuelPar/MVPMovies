package com.diego.mvpretrosample.repository

import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.source.MoviesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultMoviesRepository(
    private val moviesLocalDataSource: MoviesDataSource,
    private val moviesRemoteDataSource: MoviesDataSource,
    private val moviesRemoteMediatorDataSource: MoviesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesRepository {

    override fun getMovies() = moviesRemoteMediatorDataSource.getMovies()

    override suspend fun getMovieById(movieId: Int) = withContext(ioDispatcher) {
        moviesRemoteDataSource.getMovieById(movieId)
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        withContext(ioDispatcher) {
            moviesLocalDataSource.insertMovieDetail(movieDetail)
        }
    }

    override suspend fun getMovieDetailById(movieDetailId: Int) = withContext(ioDispatcher) {
        moviesLocalDataSource.getMovieDetailById(movieDetailId)
    }
}