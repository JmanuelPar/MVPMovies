package com.diego.mvpretrosample.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.diego.mvpretrosample.data.source.local.MoviesLocalDataSource
import com.diego.mvpretrosample.data.source.remote.MoviesRemoteDataSource
import com.diego.mvpretrosample.data.source.remoteMediator.MoviesRemoteMediatorDataSource
import com.diego.mvpretrosample.db.MovieDetailDao
import com.diego.mvpretrosample.db.MoviesRoomDatabase
import com.diego.mvpretrosample.network.TmdbApi
import com.diego.mvpretrosample.repository.DefaultMoviesRepository
import com.diego.mvpretrosample.repository.MoviesRepository

object ServiceLocator {

    private val lock = Any()
    private var database: MoviesRoomDatabase? = null

    @Volatile
    var moviesRepository: MoviesRepository? = null
        @VisibleForTesting set

    fun provideMoviesRepository(context: Context): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMoviesRepository(context)
        }
    }

    private fun createMoviesRepository(context: Context): MoviesRepository {
        val moviesRoomDatabase = database ?: createMoviesDatabase(context)
        val newRepo = DefaultMoviesRepository(
            moviesLocalDataSource = createMoviesLocalDataSource(moviesRoomDatabase.movieDetailDao()),
            moviesRemoteDataSource = createMoviesRemoteDataSource(),
            moviesRemoteMediatorDataSource = createMoviesRemoteMediatorDataSource(moviesRoomDatabase)
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesRemoteDataSource(): MoviesRemoteDataSource {
        return MoviesRemoteDataSource(apiService = createApiService())
    }

    private fun createMoviesRemoteMediatorDataSource(moviesRoomDatabase: MoviesRoomDatabase) =
        MoviesRemoteMediatorDataSource(
            apiService = createApiService(),
            moviesRoomDatabase = moviesRoomDatabase
        )

    private fun createMoviesLocalDataSource(movieDetailDao: MovieDetailDao) =
        MoviesLocalDataSource(movieDetailDao)

    private fun createApiService() = TmdbApi.retrofitService

    private fun createMoviesDatabase(context: Context): MoviesRoomDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            MoviesRoomDatabase::class.java,
            "movies_database"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            moviesRepository = null
        }
    }
}
