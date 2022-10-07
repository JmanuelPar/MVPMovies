package com.diego.mvpretrosample

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.db.MovieDatabase
import com.diego.mvpretrosample.repository.MoviesRepository
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FakeAndroidMoviesRepository : MoviesRepository {

    private lateinit var listMovieDatabase: List<MovieDatabase>
    private lateinit var movieDetail: MovieDetail
    var isIOException = false

    override fun getMovies() = flow {
        val pagingData = PagingData.from(listMovieDatabase)
        emit(pagingData)
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        return if (isIOException) ApiResult.Error(exception = IOException())
        else ApiResult.Success(data = movieDetail)
    }

    fun setListMovieDatabase(list: List<MovieDatabase>) {
        listMovieDatabase = list
    }

    fun setMovieDetail(item: MovieDetail) {
        movieDetail = item
    }
}