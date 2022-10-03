package com.diego.mvpretrosample.db

import java.util.concurrent.atomic.AtomicInteger

class RemoteKeysFactory {
    private val counter = AtomicInteger(0)

    fun createRemoteKey(): RemoteKeys {
        val id = counter.incrementAndGet()
        return RemoteKeys(
            movieId = id,
            prevKey = null,
            nextKey = 2
        )
    }
}