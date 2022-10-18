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
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.utils.Constants.API_BACKDROP_BASE_URL
import com.diego.mvpretrosample.utils.Constants.API_IMG_BASE_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun ImageView.setImage(img: String, baseUrl: Boolean) {
    val path = if (baseUrl) API_IMG_BASE_URL else API_BACKDROP_BASE_URL
    val imgUri = (path + img).toUri()
        .buildUpon()
        .scheme("https")
        .build()

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

fun TextView.setItem(item: String) {
    text = item.ifEmpty { context.getString(R.string.not_specified) }
}

fun TextView.setReleaseDate(releaseDate: String) {
    text = try {
        when {
            releaseDate.isEmpty() -> context.getString(R.string.not_specified)
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE)
                LocalDate.parse(
                    releaseDate,
                    DateTimeFormatter.ISO_DATE
                ).format(formatter)
            }
        }
    } catch (e: Exception) {
        context.getString(R.string.not_specified)
    }
}

fun TextView.setRating(rating: Double) {
    text = if (rating == -1.0) context.getString(R.string.not_rated) else rating.toString()
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