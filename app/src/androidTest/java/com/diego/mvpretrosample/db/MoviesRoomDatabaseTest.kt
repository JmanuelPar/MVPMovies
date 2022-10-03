package com.diego.mvpretrosample.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MoviesRoomDatabaseTest : TestCase() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var moviesRoomDatabase: MoviesRoomDatabase
    private lateinit var moviesDao: MoviesDao
    private lateinit var remoteKeysDao: RemoteKeysDao
    private lateinit var listMovieDatabase: List<MovieDatabase>
    private lateinit var listRemotesKey: List<RemoteKeys>

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        moviesRoomDatabase = Room.inMemoryDatabaseBuilder(
            context,
            MoviesRoomDatabase::class.java
        ).allowMainThreadQueries().build()

        moviesDao = moviesRoomDatabase.moviesDao()
        remoteKeysDao = moviesRoomDatabase.remoteKeysDao()
        val movieDatabaseFactory = MovieDatabaseFactory()
        val remoteKeysFactory = RemoteKeysFactory()

        listMovieDatabase = listOf(
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase(),
            movieDatabaseFactory.createMovieDatabase()
        )

        listRemotesKey = listOf(
            remoteKeysFactory.createRemoteKey(),
            remoteKeysFactory.createRemoteKey(),
            remoteKeysFactory.createRemoteKey(),
            remoteKeysFactory.createRemoteKey()
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        moviesRoomDatabase.clearAllTables()
    }

    @Test
    @Throws(Exception::class)
    fun insertAllMovies() = runBlocking {
        moviesDao.insertAll(listMovieDatabase)
        val listFromDb = moviesDao.getListMovies()

        assertEquals(listMovieDatabase, listFromDb)
    }

    @Test
    @Throws(Exception::class)
    fun clearMovies() = runBlocking {
        moviesDao.insertAll(listMovieDatabase)
        moviesDao.clearMovies()
        val listFromDb = moviesDao.getListMovies()

        assertEquals(true, listFromDb.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun insertAllRemoteKeys() = runBlocking {
        remoteKeysDao.insertAll(listRemotesKey)
        val listFromDb = remoteKeysDao.getListRemoteKeys()

        assertEquals(listRemotesKey, listFromDb)
    }

    @Test
    @Throws(Exception::class)
    fun remoteKeysMovieId() = runBlocking {
        // movieId in database for RemoteKeys : 1, 2, 3, 4
        remoteKeysDao.insertAll(listRemotesKey)
        val remoteKeysFromDb = remoteKeysDao.remoteKeysMovieId(movieId = 1)
        val remoteKeys = RemoteKeys(
            movieId = 1,
            prevKey = null,
            nextKey = 2
        )

        assertEquals(remoteKeys, remoteKeysFromDb)
    }

    @Test
    @Throws(Exception::class)
    fun remoteKeysMovieIdNull() = runBlocking {
        // movieId in database for RemoteKeys : 1, 2, 3, 4
        remoteKeysDao.insertAll(listRemotesKey)
        val remoteKeysFromDb = remoteKeysDao.remoteKeysMovieId(movieId = 5)

        assertEquals(null, remoteKeysFromDb)
    }

    @Test
    @Throws(Exception::class)
    fun clearRemoteKeys() = runBlocking {
        remoteKeysDao.insertAll(listRemotesKey)
        remoteKeysDao.clearRemoteKeys()
        val listFromDb = remoteKeysDao.getListRemoteKeys()

        assertEquals(true, listFromDb.isEmpty())
    }
}