package com.diego.mvpretrosample.data.source.local

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.db.MovieDatabase
import com.diego.mvpretrosample.db.MovieDetailDao
import kotlinx.coroutines.flow.Flow

class MoviesLocalDataSource internal constructor(
    private val movieDetailDao: MovieDetailDao
) : MoviesDataSource {

    override fun getMovies(): Flow<PagingData<MovieDatabase>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        movieDetailDao.insert(movieDetail)
    }

    override suspend fun getMovieDetailById(movieDetailId: Int) =
        movieDetailDao.getMovieDetailById(movieDetailId)
}