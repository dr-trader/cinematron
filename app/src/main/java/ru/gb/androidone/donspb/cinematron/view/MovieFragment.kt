package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
//import coil.api.load
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.databinding.MovieFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.*
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieModelView
import java.time.LocalDate

class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieModelView by lazy {
        ViewModelProvider(this).get(MovieModelView::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieID = arguments?.getInt(Consts.BUNDLE_ID_NAME) ?: 0
        viewModel.movieLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
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
                 //binding.mainView.showSnackBar()
                 // TODO: show error message + action
             }
         }
     }

    private fun showMovieData(oneMovie: OneMovie) {
         with (binding) {
             mainView.visibility = View.VISIBLE
             loadingLayout.visibility = View.GONE
             val date = LocalDate.parse(oneMovie.release_date)
             var genresString = ""
             for (genre in oneMovie.genres) {
                 genresString += genre.name + " "
             }
             movieTitle.text = "${oneMovie.title} (${date.year})"
             movieGenres.text = genresString
             moviePoster.load("https://image.tmdb.org/t/p/original/${oneMovie.poster_path}")
             movieRating.text = oneMovie.vote_average.toString()
             movieDecr.text = oneMovie.overview
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}