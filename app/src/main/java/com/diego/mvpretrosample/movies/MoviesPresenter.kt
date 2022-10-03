package com.diego.mvpretrosample.movies

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.repository.MoviesRepository
import com.diego.mvpretrosample.utils.transformAsMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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
        fetchMovies()
    }

    override fun fetchMovies() {
        when {
            this::currentMoviesResult.isInitialized -> {
                showMovies()
                showUI()
            }
            else -> {
                currentMoviesResult = repository.getMovies()
                    .map { pagingData ->
                        pagingData.transformAsMovie()
                    }.cachedIn(scope)
                showMovies()
                showUI()
            }
        }
    }

    override fun showMovies() {
        moviesView.showMovies(currentMoviesResult)
    }

    override fun showUI() {
        moviesView.showUI()
    }

    override fun cleanup() {
        scope.cancel()
    }
}