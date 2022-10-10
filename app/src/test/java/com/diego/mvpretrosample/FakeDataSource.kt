package com.diego.mvpretrosample

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.db.MovieDatabase
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeDataSource(
    private val listMovieDatabase: List<MovieDatabase>,
    private val movieDetail: MovieDetail?,
    private var listMovieDetailDatabase: MutableList<MovieDetail> = mutableListOf()
) : MoviesDataSource {

    override fun getMovies() = flow {
        if (listMovieDatabase.isEmpty()) emit(PagingData.empty())
        else emit(PagingData.from(listMovieDatabase))
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        movieDetail?.let {
            return ApiResult.Success(data = it)
        }

        return ApiResult.Error(exception = IOException("We have an Exception"))
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        listMovieDetailDatabase.add(movieDetail)
    }

    override suspend fun getMovieDetailById(movieDetailId: Int): MovieDetail? {
        listMovieDetailDatabase.firstOrNull { it.id == movieDetailId }?.let {
            return it
        }

        return null
    }
}