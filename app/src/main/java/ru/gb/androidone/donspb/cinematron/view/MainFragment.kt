package ru.gb.androidone.donspb.cinematron.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.androidone.donspb.cinematron.Consts
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.MainFragmentBinding
import ru.gb.androidone.donspb.cinematron.map.MapsFragment
import ru.gb.androidone.donspb.cinematron.model.MovieListItem
import ru.gb.androidone.donspb.cinematron.viewmodel.AppState
import ru.gb.androidone.donspb.cinematron.viewmodel.LocalViewModel
import ru.gb.androidone.donspb.cinematron.viewmodel.MainViewModel
import ru.gb.androidone.donspb.cinematron.viewmodel.MovieListsEnum
import java.io.IOException

private const val REQUEST_CODE = 85
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val viewModelRecent: LocalViewModel by lazy {
        ViewModelProvider(this).get(LocalViewModel::class.java)
    }

    private val adapterRecent = movieRecycler()
    private val adapterNow = movieRecycler()
    private val adapterTop = movieRecycler()
    private val adapterPop = movieRecycler()
    private val adapterUp = movieRecycler()

    private fun movieRecycler() = MovieRecycler(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: MovieListItem) {
            viewModelRecent.saveMovieToDB(movie)
            val manager = activity?.supportFragmentManager?.apply {
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
        with(binding) {
            movieRecycler.adapter = adapterRecent
            movieRecyclerNow.adapter = adapterNow
            movieRecyclerTop.adapter = adapterTop
            movieRecyclerPop.adapter = adapterPop
            movieRecyclerUp.adapter = adapterUp
            mainFragmentLocationFAB.setOnClickListener { checkPermission() }
        }

        viewModelRecent.recentLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it, R.string.recent_list)
        })
        viewModel.movieListDataNow.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.NowPlayingList.listNameId) })
        viewModel.movieListDataPop.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.PopularList.listNameId) })
        viewModel.movieListDataTop.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.TopRatedList.listNameId) })
        viewModel.movieListDataUp.observe(viewLifecycleOwner, Observer {
            renderData(it, MovieListsEnum.UpcomingList.listNameId) })
        viewModelRecent.getRecentMovies()
        viewModel.getMovieListFromRemote(MovieListsEnum.NowPlayingList)
        viewModel.getMovieListFromRemote(MovieListsEnum.PopularList)
        viewModel.getMovieListFromRemote(MovieListsEnum.TopRatedList)
        viewModel.getMovieListFromRemote(MovieListsEnum.UpcomingList)

    }

    private fun renderData(appState: AppState, listname: Int) {
        when (appState) {
            is AppState.Starting -> {
                binding.rvLoadingLayout.visibility = View.GONE
                binding.mainScrollview.visibility = View.VISIBLE
                when (listname) {
                    R.string.recent_list -> {
                        adapterRecent.setMovie(appState.movieList.results)
                    }
                    R.string.now_playing_list -> {
                        adapterNow.setMovie(appState.movieList.results)
                    }
                    R.string.popular_list -> {
                        adapterPop.setMovie(appState.movieList.results)
                    }
                    R.string.top_rated_list -> {
                        adapterTop.setMovie(appState.movieList.results)
                    }
                    R.string.upcoming_list -> {
                        adapterUp.setMovie(appState.movieList.results)
                    }
                }
            }
            is AppState.Loading -> {
                binding.mainScrollview.visibility = View.GONE
                binding.rvLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainScrollview.showSnackBar(getString(R.string.error))
            }
        }
    }

    override fun onDestroyView() {
        adapterNow.removeListener()
        adapterRecent.removeListener()
        adapterTop.removeListener()
        adapterPop.removeListener()
        adapterUp.removeListener()
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: MovieListItem)
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                            getLocation()
                        }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ->
                {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_message))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access))
                { _, _ -> requestPermission() }
                .setNegativeButton(getString(R.string.dialog_rationale_decline))
                {dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun checkPermissionsResult (requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResults.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close))
                { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
            {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as
                        LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_off),
                            getString(R.string.dialog_message_last_loc_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_off),
                            getString(R.string.dialog_message_last_loc_known)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val address = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.mainFragmentLocationFAB.post {
                    showAddressDialog(address[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setNegativeButton(getString(R.string.dialog_button_close))
                { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(getString(R.string.dialog_open_map))
                { _, _ ->
                    activity?.supportFragmentManager?.apply {
                        beginTransaction()
                            .replace(R.id.main_container, MapsFragment())
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
                .create()
                .show()
        }
    }
}

private fun View.showSnackBar(
    text: String,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).show()
}