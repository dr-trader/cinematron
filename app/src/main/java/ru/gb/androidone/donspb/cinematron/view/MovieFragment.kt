package ru.gb.androidone.donspb.cinematron.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.MovieFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.*
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

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA ID EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TITLE_EXTRA = "TITLE"
const val DETAILS_GENRES_EXTRA = "GENRES"
const val DETAILS_ID_EXTRA = "MOVIE ID"
const val DETAILS_OVERVIEW_EXTRA = "OVERVIEW"
const val DETAILS_YEAR_EXTRA = "YEAR"
const val DETAILS_VOTES_EXTRA = "VOTES"



class MovieFragment : Fragment() {

    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!
    private var movieID: Int = 0

    private val loadResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO()
                DETAILS_DATA_EMPTY_EXTRA -> TODO()
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO()
                DETAILS_REQUEST_ERROR_EXTRA -> TODO()
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO()
                DETAILS_URL_MALFORMED_EXTRA -> TODO()
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    MovieDTO(
                        intent.getStringExtra(DETAILS_TITLE_EXTRA),
                        intent.getStringExtra(DETAILS_GENRES_EXTRA),
                        intent.getIntExtra(DETAILS_ID_EXTRA, 0),
                        intent.getStringExtra(DETAILS_OVERVIEW_EXTRA),
                        intent.getIntExtra(DETAILS_YEAR_EXTRA, 0),
                        intent.getFloatExtra(DETAILS_VOTES_EXTRA, 0f),
                    )
                )
                else -> TODO()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultReceiver,
                IntentFilter(DETAILS_INTENT_FILTER)
                )
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultReceiver)
        }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }



//    private val onLoadListener: MovieLoader.MovieLoaderListener =
//        object : MovieLoader.MovieLoaderListener {
//            override fun onLoaded(oneMovie: OneMovie) {
//                showMovieData(oneMovie)
//            }
//
//            override fun onFailed(throwable: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieID = arguments?.getInt(Consts.BUNDLE_ID_NAME) ?: 0 // TODO: replace magic
        showMovieData()
    }

    companion object {
        fun newInstance(bundle: Bundle) : MovieFragment {
            val fragment = MovieFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun showMovieData() {
        with(binding) {
            mainView.visibility = View.GONE
            loadingLayout.visibility = View.VISIBLE
            context?.let {
                it.startService(Intent(it, DownloadService::class.java).apply {
                    putExtra(Consts.ID_NAME_EXTRA, movieID)
                })
            }
        }
    }

     private fun renderData(movieDTO: MovieDTO) {
         with (binding) {
             mainView.visibility = View.VISIBLE
             loadingLayout.visibility = View.GONE
             movieTitle.text = "${movieDTO.title} (${movieDTO.year})"
             movieGenres.text = movieDTO.genres
             moviePoster.setImageResource(R.drawable.demo_poster)
             movieRating.text = movieDTO.rating.toString()
             movieDecr.text = movieDTO.overview
        }
    }

}