package com.diego.mvpretrosample.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.diego.mvpretrosample.data.MovieDetail

@Database(
    entities = [MovieDatabase::class, RemoteKeys::class, MovieDetail::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class MoviesRoomDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun movieDetailDao(): MovieDetailDao
}