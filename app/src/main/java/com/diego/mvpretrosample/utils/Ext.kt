package com.diego.mvpretrosample.utils

import androidx.paging.PagingData
import androidx.paging.map
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.data.NetworkMovieDetail
import com.diego.mvpretrosample.data.NetworkMovies
import com.diego.mvpretrosample.db.MovieDatabase

// Convert NetworkMovies to List MovieDatabase
fun NetworkMovies.asDatabaseModel() =
    results?.map { result ->
        MovieDatabase(
            idMovie = result.id!!,
            title = result.title ?: "",
            posterPath = result.posterPath ?: "",
            releaseDate = result.releaseDate ?: "",
            rating = result.voteAverage ?: -1.0
        )
    } ?: emptyList()

// Convert NetworkMovieDetail to MovieDetail
fun NetworkMovieDetail.asDomainModel(): MovieDetail {
    val genres = this.genres?.let { list ->
        list.joinToString(separator = " - ") { genre -> "${genre.name}" }
    } ?: ""

    return MovieDetail(
        id = this.id!!,
        title = this.title ?: "",
        releaseDate = this.releaseDate ?: "",
        genres = genres,
        tagLine = this.tagline ?: "",
        overview = this.overview ?: "",
        rating = this.voteAverage ?: -1.0,
        backdropPath = this.backdropPath ?: ""
    )
}

fun PagingData<MovieDatabase>.transformAsMovie(): PagingData<Movie> {
    return this.map { movieDatabase ->
        Movie(
            idMovie = movieDatabase.idMovie,
            title = movieDatabase.title,
            posterPath = movieDatabase.posterPath,
            releaseDate = movieDatabase.releaseDate,
            rating = movieDatabase.rating
        )
    }
}
