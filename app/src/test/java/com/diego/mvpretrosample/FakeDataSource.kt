package com.diego.mvpretrosample

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.db.MovieDatabase
import kotlinx.coroutines.flow.Flow

class FakeDataSource(var movieDetail: MovieDetail?) : MoviesDataSource {

    override fun getMovies(): Flow<PagingData<MovieDatabase>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        movieDetail?.let {
            return ApiResult.Success(data = it)
        }

        return ApiResult.Error(exception = Exception("We have an Exception"))
    }
}