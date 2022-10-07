package com.diego.mvpretrosample.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.utils.Constants.API_IMG_BASE_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@BindingAdapter("movieImage")
fun ImageView.setMovieImage(item: Movie?) {
    item?.let { movie ->
        val imgUri = (API_IMG_BASE_URL + movie.posterPath).toUri()
            .buildUpon().scheme("https").build()
        this.load(imgUri) {
            crossfade(true)
            listener(
                onStart = {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setImageResource(R.drawable.loading_animation)
                },
                onSuccess = { _, _ ->
                    scaleType = ImageView.ScaleType.FIT_XY
                },
                onError = { _, _ ->
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setImageResource(R.drawable.ic_place_holder)
                }
            )
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
        text = try {
            when {
                movie.releaseDate.isEmpty() -> context.getString(R.string.not_specified)
                else -> {
                    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE)
                    LocalDate.parse(movie.releaseDate, DateTimeFormatter.ISO_DATE).format(formatter)
                }
            }
        } catch (e: Exception) {
            context.getString(R.string.not_specified)
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