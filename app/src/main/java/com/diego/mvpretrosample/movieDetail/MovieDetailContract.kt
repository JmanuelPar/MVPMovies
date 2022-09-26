package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.BasePresenter
import com.diego.mvpretrosample.BaseView
import com.diego.mvpretrosample.data.MovieDetail
import java.lang.Exception

interface MovieDetailContract {

    interface View : BaseView<Presenter> {
        fun configure()
        fun showProgressBar(visibility: Boolean)
        fun showLayoutResult(visibility: Boolean)
        fun showMovieDetail(movieDetail: MovieDetail)
        fun showErrorMessage(exception: Exception)
        fun showLayoutError(visibility: Boolean)
    }

    interface Presenter : BasePresenter {
        fun getMovieDetail()
        fun showProgress()
        fun showSuccess(movieDetail: MovieDetail)
        fun showError(exception: Exception)
        fun cleanUp()
    }
}