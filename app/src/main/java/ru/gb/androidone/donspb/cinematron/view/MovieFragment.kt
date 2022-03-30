package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.GrayscaleTransformation
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.databinding.MovieFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.*
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieModelView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieModelView by lazy {
        ViewModelProvider(this).get(MovieModelView::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieID = arguments?.getInt(Consts.BUNDLE_ID_NAME) ?: 0
        viewModel.movieLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getMovieFromRemoteSource(movieID)
    }

    companion object {
        fun newInstance(bundle: Bundle) : MovieFragment {
            val fragment = MovieFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

     private fun renderData(appState: AppState) {
         when (appState) {
             is AppState.Success -> {
                 binding.mainView.visibility = View.VISIBLE
                 binding.loadingLayout.visibility = View.GONE
                 showMovieData(appState.movieData)
             }
             is AppState.Loading -> {
                 binding.mainView.visibility = View.GONE
                 binding.loadingLayout.visibility = View.VISIBLE
             }
             is AppState.Error -> {
                 binding.mainView.visibility = View.VISIBLE
                 binding.loadingLayout.visibility = View.GONE
                 // TODO: show error message
             }
         }
     }

    private fun showMovieData(oneMovie: OneMovie) {
         with (binding) {
             backdrop.load(Consts.BASE_IMAGE_URL + oneMovie.backdrop_path) {
                 transformations(GrayscaleTransformation())
                 build()
             }
             mainView.visibility = View.VISIBLE
             loadingLayout.visibility = View.GONE
             val formatter = DateTimeFormatter.ofPattern("dd LLL yyyy")
             val releaseDate = "Released: " + LocalDate.parse(
                 oneMovie.release_date).format(formatter).toString()
             var genresString = ""
             for (genre in oneMovie.genres) {
                 genresString += genre.name + " "
             }
             val duration = (oneMovie.runtime / 60).toString() + "h " +
                     (oneMovie.runtime % 60) + "m"

             movieTitle.text = oneMovie.title
             movieRelease.text = releaseDate
             movieDuration.text = duration
             movieGenres.text = genresString
             moviePoster.load(Consts.BASE_IMAGE_URL + oneMovie.poster_path)
             movieRating.text = "User Score: ${oneMovie.vote_average}"
             movieDecr.text = oneMovie.overview
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}