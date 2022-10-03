package com.diego.mvpretrosample

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.db.MovieDatabase
import com.diego.mvpretrosample.repository.MoviesRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeMoviesRepository : MoviesRepository {

    private lateinit var listMovieDatabase: List<MovieDatabase>
    private lateinit var movieDetail: MovieDetail
    var failureMsg: String? = null

    override fun getMovies() = flow {
        val pagingData = PagingData.from(listMovieDatabase)
        emit(pagingData)
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        failureMsg?.let { errorMessage ->
            return ApiResult.Error(exception = IOException(errorMessage))
        }

        return ApiResult.Success(data = movieDetail)
    }

    fun setListMovieDatabase(list: List<MovieDatabase>) {
        listMovieDatabase = list
    }

    fun setMovieDetail(item: MovieDetail) {
        movieDetail = item
    }
}