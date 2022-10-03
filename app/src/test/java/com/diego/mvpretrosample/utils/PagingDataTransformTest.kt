package com.diego.mvpretrosample.utils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.diego.mvpretrosample.MovieDatabaseFactory
import com.diego.mvpretrosample.MovieFactory
import com.diego.mvpretrosample.MyMovieDiffCallback
import com.diego.mvpretrosample.NoopListCallback
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.db.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

//https://developer.android.com/topic/libraries/architecture/paging/test

@ExperimentalCoroutinesApi
class PagingDataTransformTest {

    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler)
    private lateinit var listMovieDatabase: List<MovieDatabase>
    private lateinit var listMovie: List<Movie>

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val movieDatabaseFactory = MovieDatabaseFactory()
        val movieFactory = MovieFactory()
        listMovieDatabase = listOf(
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase()
        )

        listMovie = listOf(
            movieFactory.createMovie(),
            movieFactory.createMovie(),
            movieFactory.createMovie(),
            movieFactory.createMovie()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun differTransformsData() = testScope.runTest {
        val data = PagingData.from(listMovieDatabase).transformAsMovie()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyMovieDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(data)
        advanceUntilIdle()

        assertEquals(listMovie, differ.snapshot().items)
    }
}