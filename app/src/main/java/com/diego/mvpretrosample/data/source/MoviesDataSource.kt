package com.diego.mvpretrosample.data.source

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDatabase
import com.diego.mvpretrosample.data.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MoviesDataSource {

    fun getMovies(): Flow<PagingData<MovieDatabase>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>
}