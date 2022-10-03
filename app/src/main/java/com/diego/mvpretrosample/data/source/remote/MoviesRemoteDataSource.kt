package com.diego.mvpretrosample.data.source.remote

import androidx.paging.PagingData
import com.diego.mvpretrosample.BuildConfig
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.source.MoviesDataSource
import com.diego.mvpretrosample.db.MovieDatabase
import com.diego.mvpretrosample.network.TmdbApiService
import com.diego.mvpretrosample.utils.Constants
import com.diego.mvpretrosample.utils.asDomainModel
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesRemoteDataSource internal constructor(
    private val apiService: TmdbApiService
) : MoviesDataSource {

    override fun getMovies(): Flow<PagingData<MovieDatabase>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieById(movieId: Int) =
        try {
            val movie = apiService.getMovieById(
                apiKey = BuildConfig.API_KEY,
                movieId = movieId,
                language = Constants.LANGUAGE
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