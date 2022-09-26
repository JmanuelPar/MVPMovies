package com.diego.mvpretrosample.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.diego.mvpretrosample.BuildConfig
import com.diego.mvpretrosample.data.*
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.network.TmdbApiService
import com.diego.mvpretrosample.utils.Constants.LANGUAGE
import com.diego.mvpretrosample.utils.Constants.NETWORK_TMDB_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesRemoteDataSource internal constructor(
    private val apiService: TmdbApiService
) : MoviesDataSource {

    override fun getMovies() =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TmdbPagingSourceMovies(apiService)
            }
        ).flow

    override suspend fun getMovieById(movieId: Int) =
        try {
            val movie = apiService.getMovieById(
                movieId = movieId,
                apiKey = BuildConfig.API_KEY,
                language = LANGUAGE
            ).asDomainModel()
            ApiResult.Success(data = movie)
        } catch (exception: IOException) {
            Timber.e("IOException on getMovieById : ${exception.localizedMessage}")
            ApiResult.Error(exception = exception)
        } catch (exception: HttpException) {
            Timber.e("HttpException on getMovieById : ${exception.localizedMessage}")
            ApiResult.Error(exception = exception)
        }
}