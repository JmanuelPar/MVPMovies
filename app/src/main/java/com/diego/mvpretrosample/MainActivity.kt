package com.diego.mvpretrosample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.diego.mvpretrosample.databinding.ActivityMainBinding
import com.diego.mvpretrosample.movies.MoviesFragment
import com.diego.mvpretrosample.movies.MoviesPresenter
import com.diego.mvpretrosample.utils.ServiceLocator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var moviesPresenter: MoviesPresenter

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        initPresenter()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun initPresenter() {
        moviesPresenter = MoviesPresenter(
            repository = ServiceLocator.provideMoviesRepository(this),
            moviesView = currentNavigationFragment as MoviesFragment?
                ?: MoviesFragment.newInstance()
        )
    }

    fun navigateToWithExtras(directions: NavDirections, extras: FragmentNavigator.Extras) {
        currentNavigationFragment?.apply {
            navController.navigate(directions, extras)
        }
    }
}