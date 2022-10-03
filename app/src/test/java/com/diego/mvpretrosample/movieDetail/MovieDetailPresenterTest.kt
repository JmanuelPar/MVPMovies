package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.MainCoroutineRule
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.FakeMoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class MovieDetailPresenterTest {

    @Mock
    private lateinit var movieDetailView: MovieDetailContract.View

    private lateinit var fakeMoviesRepository: FakeMoviesRepository
    private lateinit var movieDetailPresenter: MovieDetailPresenter
    private lateinit var movieDetail: MovieDetail
    private val messageException = "Exception error message"

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        fakeMoviesRepository = FakeMoviesRepository()
        movieDetail = MovieDetail(
            id = 1,
            title = "title_movie_detail_test",
            releaseDate = "2022-01-01",
            genres = "Genre 1 - Genre 2 - Genre 3",
            tagLine = "tagline_test",
            overview = "overview_test",
            rating = 1.0,
            backdropPath = ""
        )

        fakeMoviesRepository.setMovieDetail(movieDetail)
        movieDetailPresenter = MovieDetailPresenter(
            repository = fakeMoviesRepository,
            movieDetailView = movieDetailView,
            movieId = 1
        )
    }

    @After
    fun tearDown() {
        fakeMoviesRepository.failureMsg = null
    }

    @Test
    fun presenterToView() {
        verify(movieDetailView).presenter = movieDetailPresenter
    }

    @Test
    fun showLoadingToView() {
        movieDetailPresenter.showLoading()

        verify(movieDetailView).showLayoutResult(false)
        verify(movieDetailView).showLayoutError(false)
        verify(movieDetailView).showProgressBar(true)

        val inOrder = inOrder(movieDetailView)
        inOrder.verify(movieDetailView).showLayoutResult(false)
        inOrder.verify(movieDetailView).showLayoutError(false)
        inOrder.verify(movieDetailView).showProgressBar(true)
    }

    @Test
    fun fetchMovieDetailAndShowResultSuccess() = runTest {
        movieDetailPresenter.fetchMovieDetail()

        val captor = argumentCaptor<ApiResult<MovieDetail>>()
        verify(movieDetailView).showResult(captor.capture())

        assertEquals(
            true,
            captor.firstValue is ApiResult.Success
        )

        assertEquals(
            movieDetail,
            (captor.firstValue as ApiResult.Success).data
        )
    }

    @Test
    fun fetchMovieDetailAndShowResultError() = runTest {
        fakeMoviesRepository.failureMsg = messageException
        movieDetailPresenter.fetchMovieDetail()

        val captor = argumentCaptor<ApiResult<MovieDetail>>()
        verify(movieDetailView).showResult(captor.capture())

        assertEquals(
            true,
            captor.firstValue is ApiResult.Error
        )
        assertEquals(
            messageException,
            (captor.firstValue as ApiResult.Error).exception.localizedMessage
        )
    }
}
