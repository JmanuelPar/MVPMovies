package com.diego.mvpretrosample.data.source

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.data.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MoviesDataSource {

    fun getMovies() : Flow<PagingData<Movie>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>
}