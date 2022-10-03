package com.diego.mvpretrosample

import android.app.Application
import com.diego.mvpretrosample.repository.MoviesRepository
import com.diego.mvpretrosample.utils.ServiceLocator
import timber.log.Timber

class MyApplication : Application() {

    val moviesRepository: MoviesRepository
        get() = ServiceLocator.provideMoviesRepository(this)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}