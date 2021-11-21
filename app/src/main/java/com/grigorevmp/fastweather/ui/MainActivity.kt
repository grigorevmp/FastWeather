package com.grigorevmp.fastweather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.common.utils.Utils.ACCESS_LOCATION_PERMISSION
import com.grigorevmp.fastweather.databinding.ActivityMainBinding
import com.grigorevmp.fastweather.ui.binding.LocationListener
import com.jakewharton.threetenabp.AndroidThreeTen


private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(application);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fcvNavHostFragment) as NavHostFragment? ?: return
        navController = navHostFragment.navController
        setUpBottomNav(binding.bnvBottomNavBar)

        initGoogleMapLocation()

        if (checkLocationPermission())
            bindLocationManager()
        else
            requestLocationPermission()
    }

    private fun initGoogleMapLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private fun setUpBottomNav(bottomNav: BottomNavigationView) {
        navController?.let { navController ->
            bottomNav.setupWithNavController(navController)
            bottomNav.setOnItemReselectedListener {
            }
        }
        bottomNav.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(
                item,
                Navigation.findNavController(this, R.id.fcvNavHostFragment)
            )
        }
    }

    private fun bindLocationManager() {
        LocationListener(
            this,
            fusedLocationProviderClient,
            locationCallback
        )
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            ACCESS_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindLocationManager()
            } else
                Utils.makeToast(this, "Please, set location manually in settings")
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
