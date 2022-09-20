package com.diego.mvpretrosample.data

data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val rating: Double
) {
    override fun toString(): String {
        return "\n Movie ----> \n id : $id" +
                "\n title : $title" +
                "\n posterPath : $posterPath" +
                "\n backdropPath : $backdropPath" +
                "\n releaseDate : $releaseDate" +
                "\n rating : $rating"
    }
}
