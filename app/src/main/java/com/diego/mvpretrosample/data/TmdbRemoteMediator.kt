package com.diego.mvpretrosample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.diego.mvpretrosample.BuildConfig
import com.diego.mvpretrosample.db.MovieRoomDatabase
import com.diego.mvpretrosample.db.RemoteKeys
import com.diego.mvpretrosample.network.TmdbApiService
import com.diego.mvpretrosample.utils.Constants.LANGUAGE
import retrofit2.HttpException
import java.io.IOException

private const val TMDB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class TmdbRemoteMediator(
    private val apiService: TmdbApiService,
    private val movieRoomDatabase: MovieRoomDatabase
) : RemoteMediator<Int, MovieDatabase>() {

    private val movieDao = movieRoomDatabase.movieDao()
    private val remoteKeysDao = movieRoomDatabase.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDatabase>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosesToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: TMDB_STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                }
            }

            val response = apiService.getPopularMovies(
                apiKey = BuildConfig.API_KEY,
                page = page,
                language = LANGUAGE
            )
            val moviesList = response.asDatabaseModel()
            val endOfPaginationReached = moviesList.isEmpty()

            movieRoomDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    movieDao.clearMovies()
                }

                val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = moviesList.map { movieDatabase ->
                    RemoteKeys(
                        movieId = movieDatabase.idMovie,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertAll(remoteKeys = keys)
                movieDao.insertAll(movies = moviesList)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    // We need to get the remote key of the last Movie item loaded from the database
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieDatabase>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movieDatabase ->
                // Get the remote keys of the last item retrieved
                remoteKeysDao.remoteKeysMovieId(movieDatabase.idMovie)
            }
    }

    // We need to get the remote key of the first Movie item loaded from the database
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieDatabase>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movieDatabase ->
                // Get the remote keys of the first items retrieved
                remoteKeysDao.remoteKeysMovieId(movieDatabase.idMovie)
            }
    }

    private suspend fun getRemoteKeyClosesToCurrentPosition(state: PagingState<Int, MovieDatabase>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.idMovie?.let { movieId ->
                remoteKeysDao.remoteKeysMovieId(movieId)
            }
        }
    }
}