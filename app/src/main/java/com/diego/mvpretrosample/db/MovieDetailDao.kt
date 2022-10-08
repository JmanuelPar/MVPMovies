package com.diego.mvpretrosample.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diego.mvpretrosample.data.MovieDetail

@Dao
interface MovieDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieDetail: MovieDetail)

    @Query("SELECT * FROM movie_detail WHERE id = :movieDetailId")
    suspend fun getMovieDetailById(movieDetailId: Int): MovieDetail?
}