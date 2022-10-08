package com.diego.mvpretrosample.data

data class MovieDetail(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val genres: String,
    val tagLine: String,
    val overview: String,
    val rating: Double,
    val backdropPath: String,
) {
    override fun toString(): String {
        return "\n MovieDetail ----> " +
                "\n id : $id" +
                "\n title : $title" +
                "\n releaseDate : $releaseDate" +
                "\n genres : $genres" +
                "\n tagLine : $tagLine" +
                "\n overview : $overview" +
                "\n rating : $rating" +
                "\n backdropPath : $backdropPath"
    }
}