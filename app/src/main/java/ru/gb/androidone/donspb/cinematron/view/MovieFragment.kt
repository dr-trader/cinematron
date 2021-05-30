package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.MovieFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.Movie
import ru.gb.androidone.donspb.cinematron.model.MovieLoader
import ru.gb.androidone.donspb.cinematron.model.OneMovie
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.MainViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!

    private val onLoadListener: MovieLoader.MovieLoaderListener =
        object : MovieLoader.MovieLoaderListener {
            override fun onLoaded(oneMovie: OneMovie) {
                showMovieData(oneMovie)
            }

            override fun onFailed(throwable: Throwable) {
                TODO("Not yet implemented")
            }

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
        val movieID = arguments?.getInt(Consts.BUNDLE_ID_NAME) ?: 100 // TODO: replace magic
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        val loader = MovieLoader(onLoadListener, movieID)
        loader.loadMovieData()
    }

    companion object {
        fun newInstance(bundle: Bundle) : MovieFragment {
            val fragment = MovieFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun showMovieData(oneMovie: OneMovie) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val date = LocalDate.parse(oneMovie.release_date)
            movieTitle.text = "${oneMovie.title} (${date.year})"
            var genresString = ""
            for (genre in oneMovie.genres) {
                genresString += " ${genre.name}"
            }
            movieGenres.text = genresString
            moviePoster.setImageResource(R.drawable.demo_poster)
            movieRating.text = oneMovie.vote_average.toString()
            movieDecr.text = oneMovie.overview
        }
    }

}