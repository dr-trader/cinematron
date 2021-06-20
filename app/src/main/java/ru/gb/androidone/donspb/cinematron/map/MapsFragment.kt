package ru.gb.androidone.donspb.cinematron.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.gb.androidone.donspb.cinematron.R
import ru.gb.androidone.donspb.cinematron.databinding.FragmentMapsBinding
import java.io.IOException

private const val REQUEST_CODE = 85
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        activateMyLocation(googleMap)
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
//                        getLocation()
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
//                        getLocation()
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

//    private fun getLocation() {
//        activity?.let { context ->
//            if (ContextCompat.checkSelfPermission(
//                    context, Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//            {
//                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as
//                        LocationManager
//                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
//                    provider?.let {
//                        locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER,
//                            REFRESH_PERIOD,
//                            MINIMAL_DISTANCE,
//                            onLocationListener
//                        )
//                    }
//                } else {
//                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    if (location == null) {
//                        showDialog(
//                            getString(R.string.dialog_title_gps_off),
//                            getString(R.string.dialog_message_last_loc_unknown)
//                        )
//                    } else {
//                        getAddressAsync(context, location)
//                        showDialog(
//                            getString(R.string.dialog_title_gps_off),
//                            getString(R.string.dialog_message_last_loc_known)
//                        )
//                    }
//                }
//            } else {
//                showRationaleDialog()
//            }
//        }
//    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val address = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
//                binding.mainFragmentLocationFAB.post {
//                    activity?.supportFragmentManager?.apply {
//                        beginTransaction()
//                            .replace(R.id.main_container, MapsFragment())
//                            .addToBackStack("")
//                            .commitAllowingStateLoss()
//                    }
//                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }

    private fun initSearchByAddress() {
//        binding.buttonSearch.setOnClickListener {
//            val geoCoder = Geocoder(it.context)
//            val searchText = binding.searchAddress.toString()
//            Thread {
//                try {
//                    val addresses = geoCoder.getFromLocationName(searchText, 1)
//                    if (addresses.size > 0) {
//                        goToAddress(addresses, it, searchText)
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }.start()
//        }
    }

    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String
    ) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location,
                    15f
                )
            )
        }
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            googleMap.isMyLocationEnabled = isPermissionGranted
            googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted
        }
        checkPermission()
    }

    companion object {
        fun newInstance() = MapsFragment()
    }

}