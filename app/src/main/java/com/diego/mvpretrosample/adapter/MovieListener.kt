package com.diego.mvpretrosample.adapter

import android.view.View
import com.diego.mvpretrosample.data.Movie

interface MovieListener {
    fun onMovieClicked(view: View, movie: Movie)
}