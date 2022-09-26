package com.diego.mvpretrosample.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_detail")
data class MovieDetail(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "releaseDate") val releaseDate: String,
    @ColumnInfo(name = "genres") val genres: String,
    @ColumnInfo(name = "tagLine") val tagLine: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "backdropPath") val backdropPath: String,
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