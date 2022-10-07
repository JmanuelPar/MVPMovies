package com.diego.mvpretrosample

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.db.MovieDatabase
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeDataSource(
    private val list: List<MovieDatabase>,
    private val movieDetail: MovieDetail?
) : MoviesDataSource {

    override fun getMovies() = flow {
        if (list.isEmpty()) emit(PagingData.empty())
        else emit(PagingData.from(list))
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        movieDetail?.let {
            return ApiResult.Success(data = it)
        }

        return ApiResult.Error(exception = IOException("We have an Exception"))
    }
}