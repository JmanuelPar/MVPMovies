package com.diego.mvpretrosample

import androidx.recyclerview.widget.DiffUtil
import com.diego.mvpretrosample.data.Movie

class MyMovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}