package com.diego.mvpretrosample.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.core.net.toUri
import coil.load
import coil.transform.RoundedCornersTransformation
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.utils.Constants.API_BACKDROP_BASE_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun TextView.setMovieDetail(item: String) {
    text = item.ifEmpty { context.getString(R.string.not_specified) }
}

fun TextView.setMovieDetailReleaseDate(item: String) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE)
    text = LocalDate.parse(item, DateTimeFormatter.ISO_DATE).format(formatter)
}

fun TextView.setMovieDetailRating(rating: Double) {
    text = if (rating == -1.0) context.getString(R.string.not_rated) else rating.toString()
}

fun ImageView.setMovieDetailImage(movieDetailImage: String) {
    val imgUri = (API_BACKDROP_BASE_URL + movieDetailImage).toUri()
        .buildUpon().scheme("https").build()
    this.load(imgUri) {
        placeholder(R.drawable.loading_animation)
        error(R.drawable.ic_place_holder)
        transformations(RoundedCornersTransformation())
    }
}

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.GRAY)
    }
}