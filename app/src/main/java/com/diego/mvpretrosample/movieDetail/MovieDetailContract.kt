package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.BasePresenter
import com.diego.mvpretrosample.BaseView
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import java.lang.Exception

interface MovieDetailContract {

    interface View : BaseView<Presenter> {
        fun configure()
        fun showResult(apiResult: ApiResult<MovieDetail>)
        fun showProgressBar(visibility: Boolean)
        fun showLayoutResult(visibility: Boolean)
        fun showLayoutError(visibility: Boolean)
        fun showMovieDetail(movieDetail: MovieDetail)
        fun showErrorMessage(exception: Exception)
    }

    interface Presenter : BasePresenter {
        fun fetchMovieDetail()
        fun showLoading()
        fun showResult(apiResult: ApiResult<MovieDetail>)
        fun cleanUp()
    }
}