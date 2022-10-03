package com.diego.mvpretrosample

import com.diego.mvpretrosample.data.Movie
import java.util.concurrent.atomic.AtomicInteger

class MovieFactory {
    private val counter = AtomicInteger(0)

    fun createMovie(): Movie {
        val id = counter.incrementAndGet()
        return Movie(
            idMovie = id + 1,
            title = "title_$id",
            posterPath = "",
            releaseDate = "",
            rating = id.toDouble()
        )
    }
}