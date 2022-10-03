package com.diego.mvpretrosample.movies

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.diego.mvpretrosample.FakeAndroidMoviesRepository
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.db.MovieDatabase
import com.diego.mvpretrosample.db.MovieDatabaseFactory
import com.diego.mvpretrosample.utils.ServiceLocator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MoviesFragmentTest {

    private lateinit var repository: FakeAndroidMoviesRepository

    @Before
    fun setUp() {
        repository = FakeAndroidMoviesRepository()
        ServiceLocator.moviesRepository = repository
    }

    @After
    fun cleanup() = runTest {
        ServiceLocator.resetRepository()
        repository.haveException = false
    }

    @Test
    fun loadMovies_DisplayedInUi() = runTest {
        val movieDatabaseFactory = MovieDatabaseFactory()
        val listMovieDatabase = listOf(
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase()
        )

        repository.setListMovieDatabase(listMovieDatabase)

        launchFragmentInContainer<MoviesFragment>(
            themeResId = R.style.Theme_MVPRetroSample
        )

        onView(withId(R.id.rv_movies)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            matches(isDisplayed())

            val recyclerView = view as RecyclerView
            assertEquals(listMovieDatabase.size, recyclerView.adapter?.itemCount)
        }
    }

    @Test
    fun loadMovies_Empty_DisplayedInUi() = runTest {
        val listMovieDatabase = emptyList<MovieDatabase>()

        repository.setListMovieDatabase(listMovieDatabase)

        launchFragmentInContainer<MoviesFragment>(
            themeResId = R.style.Theme_MVPRetroSample
        )

        onView(withId(R.id.layout_no_result)).check(matches(isDisplayed()))
    }

    @Test
    fun movies_Nav_MovieDetailInUi() = runTest {
        val movieDatabase = MovieDatabase(
            id = 1,
            idMovie = 1,
            title = "title_1",
            posterPath = "",
            releaseDate = "2022-01-01",
            rating = 1.0
        )

        val movieDetail = MovieDetail(
            id = 1,
            title = "title_1",
            releaseDate = "2022-01-01",
            genres = "Genre 1 - Genre 2 - Genre 3",
            tagLine = "tagline_1",
            overview = "overview_1",
            rating = 1.0,
            backdropPath = ""
        )

        repository.setListMovieDatabase(listOf(movieDatabase))
        repository.setMovieDetail(movieDetail)

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val scenario = launchFragmentInContainer<MoviesFragment>(
            themeResId = R.style.Theme_MVPRetroSample
        )

        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.rv_movies))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, click()
                )
            )

        assertEquals(navController.currentDestination?.id, R.id.fragment_movie_detail)
    }
}