package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.repository.MoviesRepository
import kotlinx.coroutines.*
import java.lang.Exception
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
        getMovieDetail()
    }

    override fun getMovieDetail() {
        scope.launch {
            showProgress()
            when (val apiResult = repository.getMovieById(movieId = movieId)) {
                is ApiResult.Success -> showSuccess(apiResult.data)
                is ApiResult.Error -> showError(apiResult.exception)
            }
        }
    }

    override fun showProgress() {
        movieDetailView.showLayoutResult(false)
        movieDetailView.showLayoutError(false)
        movieDetailView.showProgressBar(true)
    }

    override fun showSuccess(movieDetail: MovieDetail) {
        movieDetailView.showProgressBar(false)
        movieDetailView.showLayoutResult(true)
        movieDetailView.showMovieDetail(movieDetail)
    }

    override fun showError(exception: Exception) {
        movieDetailView.showProgressBar(false)
        movieDetailView.showErrorMessage(exception)
        movieDetailView.showLayoutError(true)
    }

    override fun cleanUp() {
        scope.cancel()
    }
}