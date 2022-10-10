package com.diego.mvpretrosample

 import com.diego.mvpretrosample.data.MovieDetail
import java.util.concurrent.atomic.AtomicInteger

class MovieDetailFactory {

    private val counter = AtomicInteger(0)

    fun createMovieDetail(): MovieDetail {
        val id = counter.incrementAndGet()
        return MovieDetail(
            id = id,
            title = "title_$id",
            releaseDate = "release_date_$id",
            genres = "genres_$id",
            tagLine = "tagline_$id",
            overview = "overview_$id",
            rating = id.toDouble(),
            backdropPath = "url_backdropPath_$id"
        )
    }
}