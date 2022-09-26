package com.diego.mvpretrosample.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.diego.mvpretrosample.BuildConfig
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.data.asDomainModel
import com.diego.mvpretrosample.network.TmdbApiService
import com.diego.mvpretrosample.utils.Constants.LANGUAGE
import com.diego.mvpretrosample.utils.Constants.NETWORK_TMDB_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

private const val TMDB_STARTING_PAGE_INDEX = 1

class TmdbPagingSourceMovies(
    private val apiService: TmdbApiService
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: TMDB_STARTING_PAGE_INDEX

        return try {
            val response = apiService.getPopularMovies(
                apiKey = BuildConfig.API_KEY,
                page = page,
                language = LANGUAGE
            )
            val tmdbTotalPage =
                response.totalPages ?: (page + (params.loadSize / NETWORK_TMDB_PAGE_SIZE))
            val movies = response.asDomainModel()

            val prevKey = when (page) {
                TMDB_STARTING_PAGE_INDEX -> null
                else -> page - 1
            }

            val nextKey = when {
                page == TMDB_STARTING_PAGE_INDEX && tmdbTotalPage == 1 -> null
                page < tmdbTotalPage -> page + 1
                else -> null
            }

            LoadResult.Page(
                data = movies,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}