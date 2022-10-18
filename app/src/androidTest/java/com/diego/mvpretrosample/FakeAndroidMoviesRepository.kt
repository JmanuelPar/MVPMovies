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
    private var listMovieDetailDatabase = mutableListOf<MovieDetail>()
    private lateinit var movieDetail: MovieDetail
    var shouldReturnIOException = false

    override fun getMovies() = flow {
        val pagingData = PagingData.from(listMovieDatabase)
        emit(pagingData)
    }

    override suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail> {
        return if (shouldReturnIOException) ApiResult.Error(exception = IOException())
        else ApiResult.Success(data = movieDetail)
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        listMovieDetailDatabase.add(movieDetail)
    }

    override suspend fun getMovieDetailById(movieDetailId: Int): MovieDetail? {
        listMovieDetailDatabase.firstOrNull {
            it.id == movieDetailId
        }?.let {
            return it
        }

        return null
    }

    fun setListMovieDatabase(list: List<MovieDatabase>) {
        listMovieDatabase = list
    }

    fun setMovieDetail(item: MovieDetail) {
        movieDetail = item
    }

    fun addMovieDetailDatabase(movieDetail: MovieDetail) {
        listMovieDetailDatabase.add(movieDetail)
    }
}