package com.diego.mvpretrosample.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieDatabase(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "idMovie") val idMovie: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "posterPath") val posterPath: String,
    @ColumnInfo(name = "releaseDate") val releaseDate: String,
    @ColumnInfo(name = "rating") val rating: Double
) {
    override fun toString(): String {
        return "\n MovieDatabase ----> " +
                "\n id : $id" +
                "\n idMovie : $idMovie" +
                "\n title : $title" +
                "\n posterPath : $posterPath" +
                "\n releaseDate : $releaseDate" +
                "\n rating : $rating"
    }
}

