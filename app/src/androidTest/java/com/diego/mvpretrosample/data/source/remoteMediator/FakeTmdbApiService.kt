package com.diego.mvpretrosample.data.source.remoteMediator

import com.diego.mvpretrosample.data.*
import com.diego.mvpretrosample.network.TmdbApiService
import java.io.IOException

class FakeTmdbApiService : TmdbApiService {

    var failureMsg: String? = null
    var isEmptyList: Boolean = false

    private val listResult = mutableListOf<Result>()
    private val totalPages = 2
    private val totalResults = 40
    private val sizeResultMax = 20

    override suspend fun getPopularMovies(
        apiKey: String,
        page: Int,
        language: String
    ): NetworkMovies {
        failureMsg?.let { throw IOException(it) }

        val listResult =
            if (isEmptyList || totalPages <= page) emptyList()
            else createListResult(page)

        return NetworkMovies(
            page = page,
            results = listResult,
            totalPages = totalPages,
            totalResults = totalResults
        )
    }

    override suspend fun getMovieById(
        movieId: Int,
        apiKey: String,
        language: String
    ): NetworkMovieDetail {
        failureMsg?.let { throw IOException(it) }
        return createMovieDetail(movieId)
    }

    private fun createListResult(page: Int): List<Result> {
        val size = when {
            totalResults < sizeResultMax -> totalResults
            totalResults < page * sizeResultMax -> totalResults - sizeResultMax
            else -> sizeResultMax
        }

        val range = when (page) {
            1 -> 1..size
            else -> (page * sizeResultMax / 2) + 1..(size + sizeResultMax)
        }

        for (i in range) {
            listResult.add(createResult(index = i))
        }
        return listResult
    }

    private fun createResult(index: Int): Result {
        return Result(
            adult = null,
            backdropPath = null,
            genreIds = null,
            id = index,
            originalLanguage = null,
            originalTitle = null,
            overview = "overview_$index",
            popularity = null,
            posterPath = null,
            releaseDate = "2022-01-01",
            title = "title_$index",
            video = null,
            voteAverage = null,
            voteCount = null
        )
    }

    private fun createMovieDetail(idMovie: Int): NetworkMovieDetail {
        return NetworkMovieDetail(
            adult = null,
            backdropPath = null,
            belongsToCollection = null,
            budget = null,
            genres = null,
            homepage = null,
            id = idMovie,
            imdbId = null,
            originalLanguage = null,
            originalTitle = null,
            overview = "overview_$idMovie",
            popularity = null,
            posterPath = null,
            productionCompanies = null,
            productionCountries = null,
            releaseDate = "2022-01-01",
            revenue = null,
            runtime = null,
            spokenLanguages = null,
            status = null,
            tagline = "tagLine_$idMovie",
            title = "title_$idMovie",
            video = null,
            voteAverage = null,
            voteCount = null
        )
    }
}

