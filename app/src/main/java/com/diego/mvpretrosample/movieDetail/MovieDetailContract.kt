package com.diego.mvpretrosample.movieDetail

import com.diego.mvpretrosample.BasePresenter
import com.diego.mvpretrosample.BaseView
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.utils.UIText

interface MovieDetailContract {

    interface View : BaseView<Presenter> {
        fun configure()
        fun showProgressBar(visibility: Boolean)
        fun showLayoutResult(visibility: Boolean)
        fun showLayoutError(visibility: Boolean)
        fun showMovieDetail(movieDetail: MovieDetail)
        fun showErrorMessage(uiText: UIText)
    }

    interface Presenter : BasePresenter {
        fun fetchMovieDetail()
        fun showProgressBar(visibility: Boolean)
        fun showLayoutResult(visibility: Boolean)
        fun showMovieDetail(movieDetail: MovieDetail)
        fun showErrorMessage(uiText: UIText)
        fun showLayoutError(visibility: Boolean)
        fun cleanUp()
    }
}