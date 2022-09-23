package com.diego.mvpretrosample.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diego.mvpretrosample.data.MovieDatabase

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDatabase>)

    @Query("SELECT * FROM movies ORDER BY id ASC")
    fun getMovies(): PagingSource<Int, MovieDatabase>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}