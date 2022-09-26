package com.diego.mvpretrosample.data.source.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.diego.mvpretrosample.BuildConfig
import com.diego.mvpretrosample.data.*
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.db.MoviesRoomDatabase
import com.diego.mvpretrosample.network.TmdbApiService
import com.diego.mvpretrosample.utils.Constants.LANGUAGE
import com.diego.mvpretrosample.utils.Constants.NETWORK_TMDB_PAGE_SIZE
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesRemoteMediatorDataSource internal constructor(
    private val apiService: TmdbApiService,
    private val moviesRoomDatabase: MoviesRoomDatabase
) : MoviesDataSource {

    override fun getMovies() =

        @OptIn(ExperimentalPagingApi::class)
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = TmdbRemoteMediator(
                apiService = apiService,
                moviesRoomDatabase = moviesRoomDatabase
            ),
            pagingSourceFactory = { moviesRoomDatabase.moviesDao().getMovies() }
        ).flow

    override suspend fun getMovieById(movieId: Int) =
        try {
            val movie = apiService.getMovieById(
                apiKey = BuildConfig.API_KEY,
                movieId = movieId,
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