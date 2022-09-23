package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.repository.MoviesRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieDetailPresenter(
    private val repository: MoviesRepository,
    private val movieDetailView: MovieDetailContract.View,
    private val movieId: Int,
    context: CoroutineContext = Dispatchers.Main
) : MovieDetailContract.Presenter {

    private val scope: CoroutineScope = CoroutineScope(context + Job())

    init {
        movieDetailView.presenter = this
    }

    override fun start() {
        getMovieById()
    }

    override fun getMovieById() {
        scope.launch {
            movieDetailView.showLayoutResult(false)
            movieDetailView.showLayoutError(false)
            movieDetailView.showProgressBar(true)
            when (val apiResult = repository.getMovieById(movieId)
            ) {
                is ApiResult.Success -> {
                    movieDetailView.showProgressBar(false)
                    movieDetailView.showLayoutResult(true)
                    movieDetailView.showMovieDetail(apiResult.data)
                }
                is ApiResult.Error -> {
                    movieDetailView.showProgressBar(false)
                    movieDetailView.showErrorMessage(apiResult.exception)
                    movieDetailView.showLayoutError(true)
                }
            }
        }
    }

    override fun cleanUp() {
        scope.cancel()
    }
}