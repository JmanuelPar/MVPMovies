package com.diego.mvpretrosample.data

data class Movie(
    val idMovie: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val rating: Double
) {
    override fun toString(): String {
        return "\n Movie ----> " +
                "\n idMovie : $idMovie" +
                "\n title : $title" +
                "\n posterPath : $posterPath" +
                "\n releaseDate : $releaseDate" +
                "\n rating : $rating"
    }
}
