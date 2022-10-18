package com.diego.mvpretrosample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.databinding.ItemMovieCardBinding
import com.diego.mvpretrosample.utils.setImage
import com.diego.mvpretrosample.utils.setItem
import com.diego.mvpretrosample.utils.setRating
import com.diego.mvpretrosample.utils.setReleaseDate

class MovieAdapter(private val clickListener: MovieListener) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MOVIE_DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item, clickListener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    class MovieViewHolder private constructor(private val binding: ItemMovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie, listener: MovieListener) {
            binding.apply {
                movieImg.setImage(item.posterPath, true)
                movieTitle.setItem(item.title)
                movieReleaseDate.setReleaseDate(item.releaseDate)
                movieRating.setRating(item.rating)
                cardView.setOnClickListener { view ->
                    listener.onMovieClicked(
                        view = view,
                        movie = item
                    )
                }
                cardView.transitionName = String.format(
                    cardView.context.getString(R.string.movie_card_transition_name),
                    item.idMovie
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieCardBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return MovieViewHolder(binding)
            }
        }
    }

    companion object {
        val MOVIE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.idMovie == newItem.idMovie

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}