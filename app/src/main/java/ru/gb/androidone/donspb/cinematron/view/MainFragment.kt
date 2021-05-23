package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.MainFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.Movie
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val adapterRecent = movieRecycler()
    private val adapterNew = movieRecycler()
    private val adapterTop = movieRecycler()

    private fun movieRecycler() = MovieRecycler(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(MovieFragment.BUNDLE_EXTRA, movie)
                manager.beginTransaction()
                        .add(R.id.main_container, MovieFragment.newInstance(bundle))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.movieRecycler.adapter = adapterRecent
        binding.movieRecyclerNew.adapter = adapterNew
        binding.movieRecyclerTop.adapter = adapterTop

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromLocalSource()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Starting -> {
                adapterNew.setMovie(appState.newMovieData)
                adapterRecent.setMovie(appState.recentMovieData)
                adapterTop.setMovie(appState.topMovieData)
            }
//            is AppState.Success -> {
 //               adapter.setMovie(appState.movieData)
//            }
            is AppState.Loading -> {
            }
            is AppState.Error -> {
            }
        }
    }

    override fun onDestroyView() {
        adapterNew.removeListener()
        adapterRecent.removeListener()
        adapterTop.removeListener()
        super.onDestroyView()
    }
    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }
}

