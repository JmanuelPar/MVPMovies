package com.diego.mvpretrosample.repository

import androidx.paging.PagingData
import com.diego.mvpretrosample.data.*
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<PagingData<MovieDatabase>>

    suspend fun getMovieById(movieId: Int): ApiResult<MovieDetail>
}