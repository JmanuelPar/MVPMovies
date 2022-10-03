package com.diego.mvpretrosample.db

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Int): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @VisibleForTesting
    @Query("SELECT * FROM remote_keys")
    fun getListRemoteKeys(): List<RemoteKeys>
}