package com.diego.mvpretrosample.movieDetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.diego.mvpretrosample.MyApplication
import com.diego.mvpretrosample.R
import com.diego.mvpretrosample.data.ApiResult
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.databinding.FragmentMovieDetailBinding
import com.diego.mvpretrosample.utils.*
import com.google.android.material.transition.MaterialContainerTransform
import retrofit2.HttpException
import java.io.IOException

class MovieDetailFragment : Fragment(), MovieDetailContract.View {

    override lateinit var presenter: MovieDetailContract.Presenter

    private val args: MovieDetailFragmentArgs by navArgs()

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
        binding.buttonRetry.setOnClickListener {
            presenter.start()
        }

        presenter = MovieDetailPresenter(
            repository = (requireContext().applicationContext as MyApplication).moviesRepository,
            movieDetailView = this,
            movieId = args.movieId
        )

        presenter.start()
    }

    override fun onDestroy() {
        _binding = null
        presenter.cleanUp()
        super.onDestroy()
    }

    override fun showResult(apiResult: ApiResult<MovieDetail>) {
        when (apiResult) {
            is ApiResult.Success -> {
                showProgressBar(false)
                showLayoutResult(true)
                showMovieDetail(apiResult.data)
            }
            is ApiResult.Error -> {
                showProgressBar(false)
                showErrorMessage(apiResult.exception)
                showLayoutError(true)
            }
        }
    }

    override fun showProgressBar(visibility: Boolean) {
        binding.progressBar.isVisible = visibility
    }

    override fun showLayoutResult(visibility: Boolean) {
        binding.layoutResult.isVisible = visibility
    }

    override fun showLayoutError(visibility: Boolean) {
        binding.layoutError.isVisible = visibility
    }

    override fun showMovieDetail(movieDetail: MovieDetail) {
        binding.apply {
            movieDetailImg.setMovieDetailImage(movieDetail.backdropPath)
            movieDetailRating.setMovieDetailRating(movieDetail.rating)
            movieDetailTitle.setMovieDetail(movieDetail.title)
            movieDetailReleaseDate.setMovieDetailReleaseDate(movieDetail.releaseDate)
            movieDetailGenres.setMovieDetail(movieDetail.genres)
            movieDetailTagline.setMovieDetail(movieDetail.tagLine)
            movieDetailOverview.setMovieDetail(movieDetail.overview)
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
}