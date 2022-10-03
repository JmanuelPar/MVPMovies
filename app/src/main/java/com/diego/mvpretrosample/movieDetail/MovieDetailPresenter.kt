package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
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
        fetchMovieDetail()
    }

    override fun fetchMovieDetail() {
        scope.launch {
            showLoading()
            val apiResult = repository.getMovieById(movieId)
            showResult(apiResult)
        }
    }

    override fun showLoading() {
        movieDetailView.showLayoutResult(false)
        movieDetailView.showLayoutError(false)
        movieDetailView.showProgressBar(true)
    }

    override fun showResult(apiResult: ApiResult<MovieDetail>) {
        movieDetailView.showResult(apiResult)
    }

    override fun cleanUp() {
        scope.cancel()
    }
}