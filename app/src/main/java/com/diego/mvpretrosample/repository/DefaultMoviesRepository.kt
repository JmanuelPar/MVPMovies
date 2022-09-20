package com.diego.mvpretrosample.repository

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.source.MoviesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DefaultMoviesRepository(
    private val moviesRemoteDataSource: MoviesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesRepository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return moviesRemoteDataSource.getMovies()
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> =
        withContext(ioDispatcher) {
            moviesRemoteDataSource.getMovieById(movieId)
        }
}