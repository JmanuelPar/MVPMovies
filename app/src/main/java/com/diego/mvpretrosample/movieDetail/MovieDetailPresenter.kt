package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.repository.MoviesRepository
import com.diego.mvpretrosample.utils.UIText
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MovieDetailPresenter(
    private val repository: MoviesRepository,
    private val view: MovieDetailContract.View,
    private val id: Int,
    contextIO: CoroutineContext = Dispatchers.IO,
    private val contextMain: CoroutineContext = Dispatchers.Main
) : MovieDetailContract.Presenter {

    private val scope: CoroutineScope = CoroutineScope(contextIO + Job())

    init {
        view.presenter = this
    }

    override fun start() {
        fetchMovieDetail()
    }

    override fun fetchMovieDetail() {
        showLayoutResult(false)
        showLayoutError(false)
        showProgressBar(true)

        scope.launch {
            when (val response = repository.getMovieById(id)) {
                is ApiResult.Success -> {
                    repository.insertMovieDetail(response.data)

                    withContext(contextMain) {
                        showProgressBar(false)
                        showMovieDetail(response.data)
                        showLayoutResult(true)
                    }
                }

                is ApiResult.Error -> {
                    val movieDetailDb = repository.getMovieDetailById(id)
                    if (movieDetailDb != null) {
                        withContext(contextMain) {
                            showProgressBar(false)
                            showMovieDetail(movieDetailDb)
                            showLayoutResult(true)
                        }
                    } else {
                        val uiText = when (val exception = response.exception) {
                            is IOException -> UIText.NoConnect
                            is HttpException -> {
                                exception.localizedMessage?.let {
                                    UIText.MessageException(it)
                                } ?: UIText.UnknownError
                            }
                            else -> UIText.UnknownError
                        }

                        withContext(contextMain) {
                            showProgressBar(false)
                            showErrorMessage(uiText)
                            showLayoutError(true)
                        }
                    }
                }
            }
        }
    }

    override fun showProgressBar(visibility: Boolean) {
        view.showProgressBar(visibility)
    }

    override fun showLayoutResult(visibility: Boolean) {
        view.showLayoutResult(visibility)
    }

    override fun showLayoutError(visibility: Boolean) {
        view.showLayoutError(visibility)
    }

    override fun showMovieDetail(movieDetail: MovieDetail) {
        view.showMovieDetail(movieDetail)
    }

    override fun showErrorMessage(uiText: UIText) {
        view.showErrorMessage(uiText)
    }

    override fun cleanUp() {
        scope.cancel()
    }
}