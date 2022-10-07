package com.diego.mvpretrosample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.databinding.MoviePagingLoadStateFooterBinding
import retrofit2.HttpException
import java.io.IOException

class MovieLoadStateViewHolder(
    private val binding: MoviePagingLoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.buttonRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = when (loadState.error) {
                is IOException -> binding.errorMsg.context.getString(R.string.no_connect_message)
                is HttpException -> String.format(
                    binding.errorMsg.context.getString(R.string.error_message),
                    loadState.error.localizedMessage
                )
                else -> binding.errorMsg.context.getString(R.string.error_message_unknown)
            }
        }

        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.buttonRetry.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): MovieLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.movie_paging_load_state_footer,
                    parent,
                    false
                )

            val binding = MoviePagingLoadStateFooterBinding.bind(view)
            return MovieLoadStateViewHolder(binding, retry)
        }
    }
}