package com.diego.mvpretrosample

import androidx.recyclerview.widget.DiffUtil
import com.diego.mvpretrosample.db.MovieDatabase

class MyMovieDatabaseDiffCallback : DiffUtil.ItemCallback<MovieDatabase>() {

    override fun areItemsTheSame(oldItem: MovieDatabase, newItem: MovieDatabase): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MovieDatabase, newItem: MovieDatabase): Boolean {
        return oldItem == newItem
    }
}