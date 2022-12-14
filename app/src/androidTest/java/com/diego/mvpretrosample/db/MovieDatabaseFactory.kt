package com.diego.mvpretrosample.db

import java.util.concurrent.atomic.AtomicInteger

class MovieDatabaseFactory {
    private val counter = AtomicInteger(0)

    fun createMovieDatabase(): MovieDatabase {
        val id = counter.incrementAndGet()
        return MovieDatabase(
            id = id.toLong(),
            idMovie = id + 1,
            title = "title_$id",
            posterPath = "url_posterPath_$id",
            releaseDate = "release_date_$id",
            rating = id.toDouble()
        )
    }
}