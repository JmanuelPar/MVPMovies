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
    private val view: MoviesContract.View,
    context: CoroutineContext = Dispatchers.Main.immediate
) : MoviesContract.Presenter {

    private val scope: CoroutineScope = CoroutineScope(context + SupervisorJob())
    private lateinit var currentMoviesResult: Flow<PagingData<Movie>>

    init {
        view.presenter = this
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
        view.showMovies(currentMoviesResult)
    }

    override fun showUI() {
        view.showUI()
    }

    override fun showProgressBar(visibility: Boolean) {
        view.showProgressBar(visibility)
    }

    override fun showLayoutNoResult(visibility: Boolean) {
        view.showLayoutNoResult(visibility)
    }

    override fun showRecyclerView(visibility: Boolean) {
        view.showRecyclerView(visibility)
    }

    override fun showErrorMessage(errorMessage: String) {
        view.showErrorMessage(errorMessage)
    }

    override fun showLayoutError(visibility: Boolean) {
        view.showLayoutError(visibility)
    }

    override fun cleanup() {
        scope.cancel()
    }
}