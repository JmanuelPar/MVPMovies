package com.diego.mvpretrosample.movieDetail

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.diego.mvpretrosample.FakeAndroidMoviesRepository
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.utils.ServiceLocator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MovieDetailFragmentTest {

    private lateinit var repository: FakeAndroidMoviesRepository
    private val context by lazy {
        ApplicationProvider.getApplicationContext<Context>()
    }

    private val movieDetail = MovieDetail(
        id = 1,
        title = "title_1",
        releaseDate = "2022-01-01",
        genres = "genres_1",
        tagLine = "tagline_1",
        overview = "overview_1",
        rating = 1.0,
        backdropPath = "url_backdropPath_1"
    )

    private val movieId = 1

    @Before
    fun setUp() {
        repository = FakeAndroidMoviesRepository()
        ServiceLocator.moviesRepository = repository
    }

    @After
    fun cleanup() = runTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun movieDetail_DisplayedInUi() = runTest {
        repository.setMovieDetail(movieDetail)

        val bundle = MovieDetailFragmentArgs(movieId).toBundle()
        launchFragmentInContainer<MovieDetailFragment>(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_MVPRetroSample
        )

        onView(withId(R.id.layout_error)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.layout_result)).check(matches(isDisplayed()))

        onView(withId(R.id.movie_detail_img)).check(matches(isDisplayed()))

        onView(withId(R.id.movie_detail_rating)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_detail_rating)).check(matches(withText(movieDetail.rating.toString())))

        onView(withId(R.id.movie_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_detail_title)).check(matches(withText(movieDetail.title)))

        // On Ui : 2022-01-01 -> 01 janvier 2022
        onView(withId(R.id.movie_detail_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_detail_release_date)).check(matches(withText("01 janvier 2022")))

        onView(withId(R.id.movie_detail_genres)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_detail_genres)).check(matches(withText(movieDetail.genres)))

        onView(withId(R.id.movie_detail_tagline)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_detail_tagline)).check(matches(withText(movieDetail.tagLine)))

        onView(withId(R.id.movie_detail_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_detail_overview)).check(matches(withText(movieDetail.overview)))
    }

    @Test
    fun movieDetail_Error_DisplayedInUi() = runTest {
        repository.isIOException = true

        // IOException
        val errorMessageUi = context.getString(R.string.no_connect_message)

        val bundle = MovieDetailFragmentArgs(movieId).toBundle()
        launchFragmentInContainer<MovieDetailFragment>(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_MVPRetroSample
        )

        onView(withId(R.id.layout_error)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.layout_result)).check(matches(not(isDisplayed())))

        onView(withId(R.id.tv_error_message)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_error_message)).check(matches(withText(errorMessageUi)))
    }
}