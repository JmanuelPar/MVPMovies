package com.diego.mvpretrosample.repository

import androidx.paging.AsyncPagingDataDiffer
import com.diego.mvpretrosample.*
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.db.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultMoviesRepositoryTest {

    private lateinit var listMovieDatabase: List<MovieDatabase>
    private val movieDetail = MovieDetail(
        id = 1,
        title = "title_1",
        releaseDate = "release_date_1",
        genres = "genres_1",
        tagLine = "tagline_1",
        overview = "overview_1",
        rating = 1.0,
        backdropPath = "url_backdropPath_1"
    )

    private val errorMsg = "We have an Exception"

    private lateinit var moviesRepository: DefaultMoviesRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        val movieDatabaseFactory = MovieDatabaseFactory()
        listMovieDatabase = listOf(
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase()
        )

        moviesRepository = DefaultMoviesRepository(
            moviesRemoteDataSource = FakeDataSource(listMovieDatabase, movieDetail),
            moviesRemoteMediatorDataSource = FakeDataSource(listMovieDatabase, movieDetail),
            ioDispatcher = Dispatchers.Main
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMovies_fromRemoteMediatorDataSource() = runTest {
        val pagingData = moviesRepository.getMovies().first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MyMovieDatabaseDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(pagingData)
        advanceUntilIdle()

        val listRepo = differ.snapshot().items

        assertEquals(listMovieDatabase, listRepo)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMovies_fromRemoteDataSourceEmpty() = runTest {
        val moviesEmpty = FakeDataSource(emptyList(), movieDetail)
        moviesRepository = DefaultMoviesRepository(
            moviesRemoteDataSource = moviesEmpty,
            moviesRemoteMediatorDataSource = moviesEmpty,
            ioDispatcher = Dispatchers.Main
        )
        val pagingData = moviesRepository.getMovies().first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MyMovieDatabaseDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(pagingData)
        advanceUntilIdle()

        val isEmpty = differ.snapshot().isEmpty()

        assertEquals(true, isEmpty)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMovieById_fromRemoteDataSource() = runTest {
        val isSuccess = moviesRepository.getMovieById(movieDetail.id) is ApiResult.Success

        assertEquals(true, isSuccess)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMovieById_fromRemoteDataSourceError() = runTest {
        val movieDetailNull = FakeDataSource(listMovieDatabase, null)
        moviesRepository = DefaultMoviesRepository(
            moviesRemoteDataSource = movieDetailNull,
            moviesRemoteMediatorDataSource = movieDetailNull,
            ioDispatcher = Dispatchers.Main
        )
        val isError = moviesRepository.getMovieById(movieDetail.id) is ApiResult.Error

        assertEquals(true, isError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMovieById_fromRemoteDataSourceErrorMessage() = runTest {
        val movieDetailNull = FakeDataSource(listMovieDatabase, null)
        moviesRepository = DefaultMoviesRepository(
            moviesRemoteDataSource = movieDetailNull,
            moviesRemoteMediatorDataSource = movieDetailNull,
            ioDispatcher = Dispatchers.Main
        )
        val errorMsgRepo =
            (moviesRepository.getMovieById(movieDetail.id)
                    as ApiResult.Error).exception.localizedMessage

        assertEquals(errorMsg, errorMsgRepo)
    }
}