package com.diego.mvpretrosample.repository

import com.diego.mvpretrosample.FakeDataSource
import com.diego.mvpretrosample.MainCoroutineRule
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultMoviesRepositoryTest {

    private val movieDetail = MovieDetail(
        id = 1,
        title = "title_movie_detail_test",
        releaseDate = "2022-01-01",
        genres = "Genre 1 - Genre 2 - Genre 3",
        tagLine = "tagline_test",
        overview = "overview_test",
        rating = 1.0,
        backdropPath = ""
    )

    private val errorMsg = "We have an Exception"

    private lateinit var moviesRemoteDataSource: FakeDataSource
    private lateinit var moviesRemoteMediatorDataSource: FakeDataSource
    private lateinit var moviesRepository: DefaultMoviesRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        moviesRemoteDataSource = FakeDataSource(movieDetail)
        moviesRemoteMediatorDataSource = FakeDataSource(movieDetail)
        moviesRepository = DefaultMoviesRepository(
            moviesRemoteDataSource = moviesRemoteDataSource,
            moviesRemoteMediatorDataSource = moviesRemoteMediatorDataSource,
            ioDispatcher = Dispatchers.Main
        )
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
        val movieDetailNull = FakeDataSource(null)
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
        val movieDetailNull = FakeDataSource(null)
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