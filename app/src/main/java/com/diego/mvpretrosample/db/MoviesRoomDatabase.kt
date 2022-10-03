package com.diego.mvpretrosample.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieDatabase::class, RemoteKeys::class],
    version = 1,
    exportSchema = true
)
abstract class MoviesRoomDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}