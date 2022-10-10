package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.FakeMoviesRepository
import com.diego.mvpretrosample.MainCoroutineRule
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.utils.UIText
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

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        fakeMoviesRepository = FakeMoviesRepository()
        fakeMoviesRepository.setMovieDetail(movieDetail)
        movieDetailPresenter = MovieDetailPresenter(
            repository = fakeMoviesRepository,
            view = movieDetailView,
            id = movieDetail.id
        )
    }

    @After
    fun tearDown() {
        fakeMoviesRepository.isIOException = false
        fakeMoviesRepository.clearMovieDetailDatabase()
    }

    @Test
    fun presenterToView() {
        verify(movieDetailView).presenter = movieDetailPresenter
    }

    @Test
    fun showLoadingToView() {
        movieDetailPresenter.fetchMovieDetail()

        verify(movieDetailView).showLayoutResult(false)
        verify(movieDetailView).showLayoutError(false)
        verify(movieDetailView).showProgressBar(true)

        val inOrder = inOrder(movieDetailView)
        inOrder.verify(movieDetailView).showLayoutResult(false)
        inOrder.verify(movieDetailView).showLayoutError(false)
        inOrder.verify(movieDetailView).showProgressBar(true)
    }

    @Test
    fun showMovieDetailToView() {
        movieDetailPresenter.fetchMovieDetail()

        verify(movieDetailView).showProgressBar(false)
        verify(movieDetailView).showLayoutResult(true)

        val inOrder = inOrder(movieDetailView)
        inOrder.verify(movieDetailView).showProgressBar(false)
        inOrder.verify(movieDetailView).showLayoutResult(true)
    }

    @Test
    fun showErrorToView() {
        // No save MovieDetail in local database
        fakeMoviesRepository.isIOException = true
        movieDetailPresenter.fetchMovieDetail()

        verify(movieDetailView).showProgressBar(false)
        verify(movieDetailView).showLayoutError(true)

        val inOrder = inOrder(movieDetailView)
        inOrder.verify(movieDetailView).showProgressBar(false)
        inOrder.verify(movieDetailView).showLayoutError(true)
    }

    @Test
    fun showMovieDetailAfterErrorToView() {
        // Save MovieDetail in local database
        fakeMoviesRepository.addMovieDetailDatabase(movieDetail)
        fakeMoviesRepository.isIOException = true

        movieDetailPresenter.fetchMovieDetail()

        verify(movieDetailView).showProgressBar(false)
        verify(movieDetailView).showLayoutResult(true)

        val inOrder = inOrder(movieDetailView)
        inOrder.verify(movieDetailView).showProgressBar(false)
        inOrder.verify(movieDetailView).showLayoutResult(true)
    }

    @Test
    fun fetchMovieDetailAndShowMovieDetail() = runTest {
        movieDetailPresenter.fetchMovieDetail()

        val captor = argumentCaptor<MovieDetail>()
        verify(movieDetailView).showMovieDetail(captor.capture())

        assertEquals(movieDetail, captor.firstValue)
    }

    @Test
    fun fetchMovieDetailAndShowMovieDetailAfterError() = runTest {
        // Save MovieDetail in local database
        fakeMoviesRepository.addMovieDetailDatabase(movieDetail)
        fakeMoviesRepository.isIOException = true

        movieDetailPresenter.fetchMovieDetail()

        val captor = argumentCaptor<MovieDetail>()
        verify(movieDetailView).showMovieDetail(captor.capture())

        assertEquals(movieDetail, captor.firstValue)
    }

    @Test
    fun fetchMovieDetailAndShowErrorMsg() = runTest {
        // No save MovieDetail in local database
        fakeMoviesRepository.isIOException = true
        movieDetailPresenter.fetchMovieDetail()

        val captor = argumentCaptor<UIText>()
        verify(movieDetailView).showErrorMessage(captor.capture())

        assertEquals(UIText.NoConnect, captor.firstValue)
    }
}
