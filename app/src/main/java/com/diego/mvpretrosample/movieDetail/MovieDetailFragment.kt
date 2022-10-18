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
import com.diego.mvpretrosample.data.MovieDetail
import com.diego.mvpretrosample.databinding.FragmentMovieDetailBinding
import com.diego.mvpretrosample.utils.*
import com.google.android.material.transition.MaterialContainerTransform

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

        presenter = MovieDetailPresenter(
            repository = (requireContext().applicationContext as MyApplication).moviesRepository,
            view = this,
            id = args.movieId
        )
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

    override fun showLayoutError(visibility: Boolean) {
        binding.layoutError.isVisible = visibility
    }

    override fun showMovieDetail(movieDetail: MovieDetail) {
        binding.apply {
            movieDetailImg.setImage(movieDetail.backdropPath, false)
            movieDetailRating.setRating(movieDetail.rating)
            movieDetailTitle.setItem(movieDetail.title)
            movieDetailReleaseDate.setReleaseDate(movieDetail.releaseDate)
            movieDetailGenres.setItem(movieDetail.genres)
            movieDetailTagline.setItem(movieDetail.tagLine)
            movieDetailOverview.setItem(movieDetail.overview)
        }
    }

    override fun showErrorMessage(uiText: UIText) {
        binding.tvErrorMessage.text = requireContext().getMyUIText(uiText)
    }
}