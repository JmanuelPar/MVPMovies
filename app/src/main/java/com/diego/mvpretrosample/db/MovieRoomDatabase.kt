package com.diego.mvpretrosample.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.diego.mvpretrosample.data.MovieDatabase

@Database(
    entities = [MovieDatabase::class, RemoteKeys::class],
    version = 1,
    exportSchema = true
)
abstract class MovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: MovieRoomDatabase? = null

        fun getInstance(context: Context): MovieRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieRoomDatabase::class.java,
                "movies_database"
            ).build()
    }
}