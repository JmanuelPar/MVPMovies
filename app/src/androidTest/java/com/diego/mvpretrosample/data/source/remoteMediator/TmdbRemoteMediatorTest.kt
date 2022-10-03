package com.diego.mvpretrosample.data.source.remoteMediator

import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diego.mvpretrosample.db.MovieDatabase
import com.diego.mvpretrosample.db.MoviesRoomDatabase
import com.diego.mvpretrosample.utils.Constants.NETWORK_TMDB_PAGE_SIZE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

// https://developer.android.com/topic/libraries/architecture/paging/test

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TmdbRemoteMediatorTest {

    private lateinit var moviesRoomDatabase: MoviesRoomDatabase
    private lateinit var fakeTmdbApiService: FakeTmdbApiService
    private val messageFailure = "Throw test failure"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        moviesRoomDatabase = Room.inMemoryDatabaseBuilder(
            context,
            MoviesRoomDatabase::class.java,
        )
            .allowMainThreadQueries()
            .build()
        fakeTmdbApiService = FakeTmdbApiService()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        moviesRoomDatabase.clearAllTables()
        fakeTmdbApiService.failureMsg = null
        fakeTmdbApiService.isEmptyList = false
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenDataIsPresent() = runTest {
        val remoteMediator = TmdbRemoteMediator(
            apiService = fakeTmdbApiService,
            moviesRoomDatabase = moviesRoomDatabase
        )

        val pagingState = PagingState<Int, MovieDatabase>(
            listOf(),
            null,
            PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = true
            ),
            NETWORK_TMDB_PAGE_SIZE
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertEquals(
            true,
            result is RemoteMediator.MediatorResult.Success
        )
        assertEquals(
            false,
            (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached
        )
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoData() = runTest {
        fakeTmdbApiService.isEmptyList = true

        val remoteMediator = TmdbRemoteMediator(
            apiService = fakeTmdbApiService,
            moviesRoomDatabase = moviesRoomDatabase
        )

        val pagingState = PagingState<Int, MovieDatabase>(
            listOf(),
            null,
            PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = true
            ),
            NETWORK_TMDB_PAGE_SIZE
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertEquals(
            true,
            result is RemoteMediator.MediatorResult.Success
        )
        assertEquals(
            true,
            (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached
        )
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        fakeTmdbApiService.failureMsg = messageFailure

        val remoteMediator = TmdbRemoteMediator(
            apiService = fakeTmdbApiService,
            moviesRoomDatabase = moviesRoomDatabase
        )

        val pagingState = PagingState<Int, MovieDatabase>(
            listOf(),
            null,
            PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = true
            ),
            NETWORK_TMDB_PAGE_SIZE
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertEquals(
            true,
            result is RemoteMediator.MediatorResult.Error
        )
        assertEquals(
            messageFailure,
            (result as RemoteMediator.MediatorResult.Error).throwable.localizedMessage
        )
    }
}