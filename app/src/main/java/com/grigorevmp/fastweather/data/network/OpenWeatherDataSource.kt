package com.grigorevmp.fastweather.data.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grigorevmp.fastweather.data.network.response.OpenWeatherResponse


class OpenWeatherNetworkDataSource(
    private val application: Application
) {
    private val _downloadedOpenWeather = MutableLiveData<OpenWeatherResponse>()
    val downloadedOpenWeather: LiveData<OpenWeatherResponse>
        get() = _downloadedOpenWeather

    suspend fun fetchOpenWeather(lat: Double, lon: Double, unit: String) {
        try {
            val openWeatherApiService: OpenWeatherApi =
                OpenWeatherApi.invoke(ConnectivityInterceptorImpl(application.applicationContext))
            val fetchResponse =
                openWeatherApiService.getOpenWeatherAsync(lat = lat, lon = lon, units = unit).await()

            _downloadedOpenWeather.postValue(fetchResponse)
        } catch (e: Exception) {
            Log.e("Connectivity", "No Connectivity", e)
        }
    }
}