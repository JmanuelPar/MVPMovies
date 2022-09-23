package com.diego.mvpretrosample.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.utils.Constants.API_IMG_BASE_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("movieImage")
fun ImageView.setMovieImage(item: Movie?) {
    item?.let { movie ->
        val imgUri = (API_IMG_BASE_URL + movie.posterPath).toUri()
            .buildUpon().scheme("https").build()
        this.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_place_holder)
            transformations(RoundedCornersTransformation())
        }
    }
}

@BindingAdapter("movieTitle")
fun TextView.setMovieTitle(item: Movie?) {
    item?.let { movie ->
        text = movie.title.ifEmpty { context.getString(R.string.not_specified) }
    }
}

@BindingAdapter("movieReleaseDate")
fun TextView.setMovieReleaseDate(item: Movie?) {
    item?.let { movie ->
        text = when {
            movie.releaseDate.isEmpty() -> context.getString(R.string.not_specified)
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                LocalDate.parse(movie.releaseDate, DateTimeFormatter.ISO_DATE).format(formatter)
            }
        }
    }
}

@BindingAdapter("movieRating")
fun TextView.setMovieRating(item: Movie?) {
    item?.let { movie ->
        text =
            if (movie.rating == -1.0) context.getString(R.string.not_rated) else movie.rating.toString()
    }
}

