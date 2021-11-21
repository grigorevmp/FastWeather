package com.grigorevmp.fastweather.data.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.LocationPermissionNotGrantedException
import com.grigorevmp.fastweather.common.asDeferred
import java.io.IOException
import java.util.*
import kotlin.math.abs

interface LocationProvider {

    suspend fun hasLocationChanged(): Boolean
    suspend fun getPreferredLocationString(): String
    suspend fun getPreferredLocationCoordinates(): LatLng
    fun saveCoordinates(latLon: LatLng)

}

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) :
    LocationProvider {

    private val appContext = context.applicationContext
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
        appContext
    )
    private val ll: LatLng = LatLng(54.19609, 37.61822)
    private val defaultCity = "Тула"
    override suspend fun hasLocationChanged(): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(getCoordinates())
        } catch (e: LocationPermissionNotGrantedException) {
            return false
        }
        return deviceLocationChanged || hasCustomLocationChanged(getCoordinates())
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation()
                    ?: return "${getCustomLocationName()}"
                return getCityNameFromCoordinates(
                    LatLng(
                        deviceLocation.latitude,
                        deviceLocation.longitude
                    )
                )
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        } else {
            return "${getCustomLocationName()}"
        }
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: LatLng): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation() ?: return false

        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastWeatherLocation.latitude) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastWeatherLocation.longitude) > comparisonThreshold
    }

    private fun hasCustomLocationChanged(coordinates: LatLng): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            if (customLocationName != null) {
                return customLocationName.contentEquals(getCityNameFromCoordinates(coordinates))
            }
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastDeviceLocation(): Location? {
        return (if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException())
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun getPreferredLocationCoordinates(): LatLng {
        val cityName = getCustomLocationName() ?: defaultCity
        return if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation()
                if (deviceLocation != null) {
                    LatLng(deviceLocation.latitude, deviceLocation.longitude)
                } else {
                    getCoordinatesFromCityName(cityName)
                }
            } catch (e: LocationPermissionNotGrantedException) {
                getCoordinatesFromCityName(cityName)
            }
        } else {
            getCoordinatesFromCityName(cityName)
        }
    }

    private fun getCoordinatesFromCityName(cityName: String): LatLng {

        if (Geocoder.isPresent()) {
            try {
                val gc = Geocoder(appContext)
                val addresses: List<Address> =
                    gc.getFromLocationName(cityName, 1)
                for (a in addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        return LatLng(a.latitude, a.longitude)
                    }
                }
                return ll
            } catch (e: IOException) {
                return ll
            }
        }
        return ll
    }

    private fun getCityNameFromCoordinates(coordinates: LatLng): String {
        val gcd = Geocoder(appContext, Locale.getDefault())
        val addresses = gcd.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
        return if (addresses.size > 0) {
            addresses[0]?.locality ?: defaultCity
        } else {
            defaultCity
        }
    }

    override fun saveCoordinates(latLon: LatLng) {
        preferences.edit().putFloat(
            appContext.getString(R.string.latitude_value),
            latLon.latitude.toFloat()
        ).apply()
        preferences.edit().putFloat(
            appContext.getString(R.string.latitude_value),
            latLon.longitude.toFloat()
        ).apply()
    }

    private fun getCoordinates(): LatLng {
        return LatLng(
            preferences.getFloat(
                appContext.getString(R.string.latitude_value),
                ll.latitude.toFloat()
            ).toDouble(),
            preferences.getFloat(
                appContext.getString(R.string.latitude_value),
                ll.longitude.toFloat()
            ).toDouble()
        )
    }
}