package com.diego.mvpretrosample.movies

import androidx.paging.PagingData
import com.diego.mvpretrosample.BasePresenter
import com.diego.mvpretrosample.BaseView
import com.diego.mvpretrosample.data.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesContract {

    interface View : BaseView<Presenter> {
        fun configure()
        fun showMovies(pagingDataFlow: Flow<PagingData<Movie>>)
        fun showUI()
        fun showProgressBar(visibility: Boolean)
        fun showLayoutNoResult(visibility: Boolean)
        fun showRecyclerView(visibility: Boolean)
        fun showError(errorMessage: String)
        fun showErrorMessage(errorMessage: String)
        fun showLayoutError(visibility: Boolean)
    }

    interface Presenter : BasePresenter {
        fun getMovies()
        fun showMoviesUI()
        fun cleanup()
    }
}