package com.diego.mvpretrosample.movies

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.diego.mvpretrosample.*
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.db.MovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
class MoviesPresenterTest {

    @Mock
    private lateinit var moviesView: MoviesContract.View

    private lateinit var fakeMoviesRepository: FakeMoviesRepository
    private lateinit var moviesPresenter: MoviesPresenter
    private lateinit var listMovieDatabase: List<MovieDatabase>

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        fakeMoviesRepository = FakeMoviesRepository()
        val movieDatabaseFactory = MovieDatabaseFactory()
        listMovieDatabase = listOf(
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase()
        )

        fakeMoviesRepository.setListMovieDatabase(listMovieDatabase)
        moviesPresenter = MoviesPresenter(
            repository = fakeMoviesRepository,
            view = moviesView
        )
    }

    @Test
    fun presenterToView() {
        verify(moviesView).presenter = moviesPresenter
    }

    @Test
    fun fetchMoviesAndShowMovies() = runTest {
        moviesPresenter.fetchMovies()

        val movieArgumentCaptor = argumentCaptor<Flow<PagingData<Movie>>>()
        verify(moviesView).showMovies(movieArgumentCaptor.capture())

        val pagingDataMovieCaptor = movieArgumentCaptor.firstValue.first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyMovieDiffCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(pagingDataMovieCaptor)
        advanceUntilIdle()

        val sizeListUI = differ.snapshot().items.size
        val sizeListRepo = listMovieDatabase.size

        assertEquals(sizeListRepo, sizeListUI)
    }

    @Test
    fun fetchMoviesAndShowUI() = runTest {
        moviesPresenter.fetchMovies()
        verify(moviesView).showUI()
    }

    @Test
    fun fetchMoviesAndInOrder() = runTest {
        moviesPresenter.fetchMovies()

        val movieArgumentCaptor = argumentCaptor<Flow<PagingData<Movie>>>()

        val inOrder = inOrder(moviesView)
        inOrder.verify(moviesView).showMovies(movieArgumentCaptor.capture())
        inOrder.verify(moviesView).showUI()
    }
}