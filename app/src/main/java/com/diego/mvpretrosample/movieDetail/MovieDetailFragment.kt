package com.diego.mvpretrosample.movieDetail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import retrofit2.HttpException
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.databinding.FragmentMovieDetailBinding
import com.diego.mvpretrosample.utils.*
import com.google.android.material.transition.MaterialContainerTransform
import java.io.IOException

class MovieDetailFragment : Fragment(), MovieDetailContract.View {

    override lateinit var presenter: MovieDetailContract.Presenter

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(
            inflater,
            container,
            false
        )
        configure()
        return binding.root
    }

    override fun configure() {
        presenter = MovieDetailPresenter(
            repository = ServiceLocator.provideMoviesRepository(),
            movieDetailView = this,
            movieId = MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId
        )

        binding.buttonRetry.setOnClickListener {
            presenter.start()
        }

        presenter.start()
    }

    override fun onDestroy() {
        _binding = null
        presenter.cleanUp()
        super.onDestroy()
    }

    override fun showProgressBar(visibility: Boolean) {
        binding.progressBar.isVisible = visibility
    }

    override fun showLayoutResult(visibility: Boolean) {
        binding.layoutResult.isVisible = visibility
    }

    override fun showMovieDetail(movieDetail: MovieDetail) {
        binding.apply {
             movieImg.setMovieDetailImage(movieDetail.backdropPath)
             movieRating.setMovieDetailRating(movieDetail.rating)
             movieTitle.setMovieDetail(movieDetail.title)
             movieReleaseDate.setMovieDetailReleaseDate(movieDetail.releaseDate)
             movieGenres.setMovieDetail(movieDetail.genres)
             movieTagline.setMovieDetail(movieDetail.tagLine)
             movieOverview.setMovieDetail(movieDetail.overview)
        }
    }

    override fun showErrorMessage(exception: Exception) {
        when (exception) {
            is IOException -> {
                binding.tvErrorMessage.text = getString(R.string.no_connect_message)
            }
            is HttpException -> {
                binding.tvErrorMessage.text = String.format(
                    getString(R.string.error_result_message),
                    exception.localizedMessage
                )
            }
        }
    }

    override fun showLayoutError(visibility: Boolean) {
        binding.layoutError.isVisible = visibility
    }
}