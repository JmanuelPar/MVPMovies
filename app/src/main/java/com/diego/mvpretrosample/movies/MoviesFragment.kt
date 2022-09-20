package com.diego.mvpretrosample.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.diego.mvpretrosample.MainActivity
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.adapter.MovieAdapter
import com.diego.mvpretrosample.adapter.MovieListener
import com.diego.mvpretrosample.adapter.MovieLoadStateAdapter
import com.diego.mvpretrosample.data.Movie
import com.diego.mvpretrosample.databinding.FragmentMoviesBinding
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesFragment : Fragment(), MoviesContract.View, MovieListener {

    override lateinit var presenter: MoviesContract.Presenter

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(
            inflater,
            container,
            false
        )
        configure()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onDestroy() {
        _binding = null
        presenter.cleanup()
        super.onDestroy()
    }

    override fun configure() {
        val mRvMovies = binding.rvMovies
        movieAdapter = MovieAdapter(this)
        val headerAdapter = MovieLoadStateAdapter { movieAdapter.retry() }
        val footerAdapter = MovieLoadStateAdapter { movieAdapter.retry() }

        mRvMovies.apply {
            layoutManager = GridLayoutManager(mRvMovies.context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when {
                            position == 0 && headerAdapter.itemCount > 0 -> 2
                            position == movieAdapter.itemCount && footerAdapter.itemCount > 0 -> 2
                            else -> 1
                        }
                    }
                }
            }
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )
            setHasFixedSize(true)
        }

        binding.buttonRetry.setOnClickListener {
            showLayoutError(false)
            movieAdapter.retry()
        }

        presenter.start()
    }

    override fun showMovies(pagingDataFlow: Flow<PagingData<Movie>>) {
        viewLifecycleOwner.lifecycleScope.launch {
            pagingDataFlow.collectLatest(movieAdapter::submitData)
        }
    }

    override fun showUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0

                showProgressBar(loadState.source.refresh is LoadState.Loading)
                showLayoutNoResult(isListEmpty)
                showRecyclerView(!isListEmpty)

                val errorState = loadState.refresh as? LoadState.Error
                errorState?.let { loadStateError ->
                    val errorMessage = when (loadStateError.error) {
                        is IOException -> getString(R.string.no_connect_message)
                        is HttpException -> String.format(
                            getString(R.string.error_result_message_retry),
                            loadStateError.error.localizedMessage
                        )
                        else -> getString(R.string.error_result_message_unknown_retry)
                    }

                    showError(errorMessage)
                }
            }
        }
    }

    override fun showProgressBar(visibility: Boolean) {
        binding.progressBar.isVisible = visibility
    }

    override fun showLayoutNoResult(visibility: Boolean) {
        binding.layoutNoResult.isVisible = visibility
    }

    override fun showRecyclerView(visibility: Boolean) {
        binding.rvMovies.isVisible = visibility
    }

    override fun showError(errorMessage: String) {
        showErrorMessage(errorMessage)
        showLayoutError(true)
    }

    override fun showErrorMessage(errorMessage: String) {
        binding.tvErrorMessage.text = errorMessage
    }

    override fun showLayoutError(visibility: Boolean) {
        binding.layoutError.isVisible = visibility
    }

    override fun onMovieClicked(view: android.view.View, movie: Movie) {
        Timber.i("Movie : $movie")
        goToMovieDetailFragment(view, movie)
    }

    private fun goToMovieDetailFragment(view: View, movie: Movie) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        val movieDetailCardTransitionName = getString(R.string.movie_detail_card_transition_name)
        (activity as MainActivity).navigateToWithExtras(
            directions = MoviesFragmentDirections.actionFragmentMoviesToFragmentMovieDetail(movieId = movie.id),
            extras = FragmentNavigatorExtras(view to movieDetailCardTransitionName)
        )
    }

    companion object {
        fun newInstance() = MoviesFragment()
    }
}