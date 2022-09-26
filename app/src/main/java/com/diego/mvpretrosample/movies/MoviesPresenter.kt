package com.diego.mvpretrosample.movies

import androidx.paging.*
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.repository.MoviesRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class MoviesPresenter(
    private val repository: MoviesRepository,
    private val moviesView: MoviesContract.View,
    context: CoroutineContext = Dispatchers.Main.immediate
) : MoviesContract.Presenter {

    private val scope: CoroutineScope = CoroutineScope(context + SupervisorJob())
    private lateinit var currentMoviesResult: Flow<PagingData<Movie>>

    init {
        moviesView.presenter = this
    }

    override fun start() {
        getMovies()
    }

    override fun getMovies() {
        when {
            this::currentMoviesResult.isInitialized -> showMoviesUI()
            else -> {
                currentMoviesResult = repository.getMovies()
                    .map { pagingData ->
                        pagingData.map { movieDatabase ->
                            Movie(
                                idMovie = movieDatabase.idMovie,
                                title = movieDatabase.title,
                                posterPath = movieDatabase.posterPath,
                                releaseDate = movieDatabase.releaseDate,
                                rating = movieDatabase.rating
                            )
                        }
                    }.cachedIn(scope)
                showMoviesUI()
            }
        }
    }

    override fun showMoviesUI() {
        moviesView.showMovies(currentMoviesResult)
        moviesView.showUI()
    }

    override fun cleanup() {
        scope.cancel()
    }
}