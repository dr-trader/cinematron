package ru.gb.androidone.donspb.cinematron.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.MainFragmentBinding
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.MainViewModel
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieListsEnum

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter = MovieRecycler(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: MovieListItem) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.main_container, MovieFragment.newInstance(Bundle().apply {
                        putInt(Consts.BUNDLE_ID_NAME, movie.id)
                    }))
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
        val layoutManager = GridLayoutManager(
            requireContext(),
            2,
            RecyclerView.VERTICAL,
            false
        )

        viewModel.movieListData.observe(viewLifecycleOwner) {
            renderData(it, MovieListsEnum.TopRatedList.listNameId)
        }

        binding.movieRecycler.layoutManager = layoutManager
        binding.movieRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val childCount = layoutManager.childCount
                val visibleCount = layoutManager.findFirstVisibleItemPosition()
                val totalCount = layoutManager.itemCount
                if ((childCount + visibleCount) >= totalCount) viewModel.getMovieListFromRemote(MovieListsEnum.TopRatedList.pathPart)
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        binding.movieRecycler.adapter = adapter

        viewModel.getMovieListFromRemote(MovieListsEnum.TopRatedList.pathPart)
    }

    private fun renderData(appState: AppState, listname: Int) {
        when (appState) {
            is AppState.Starting -> {
                binding.rvLoadingLayout.visibility = View.GONE
                binding.movieRecycler.visibility = View.VISIBLE
                adapter.setMovie(appState.movieList.results)
            }
            is AppState.Loading -> {
                binding.movieRecycler.visibility = View.GONE
                binding.rvLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainLayout.showSnackBar(getString(R.string.error))
            }
        }
    }

    override fun onDestroyView() {
        adapter.removeListener()
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: MovieListItem)
    }
}

private fun View.showSnackBar(
    text: String,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).show()
}