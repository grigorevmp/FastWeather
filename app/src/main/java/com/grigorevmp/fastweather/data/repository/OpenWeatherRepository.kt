package com.grigorevmp.fastweather.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.grigorevmp.fastweather.data.db.database.ForecastDatabase
import com.grigorevmp.fastweather.data.db.entity.CurrentForecast
import com.grigorevmp.fastweather.data.db.entity.DailyForecast
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast
import com.grigorevmp.fastweather.data.network.OpenWeatherNetworkDataSource
import com.grigorevmp.fastweather.data.network.response.OpenWeatherResponse
import com.grigorevmp.fastweather.data.providers.LocationProviderImpl
import kotlinx.coroutines.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class OpenWeatherRepository(
    application: android.app.Application
) {
    private val database = ForecastDatabase.getInstance()
    private val currentDao = database.currentDao()
    private val dailyDao = database.dailyDao()
    private val hourlyDao = database.hourlyDao()
    private val openWeatherNetworkDataSource = OpenWeatherNetworkDataSource(application)
    private val locationProvider = LocationProviderImpl(
        FusedLocationProviderClient(application), application.applicationContext
    )

    init {
        openWeatherNetworkDataSource.downloadedOpenWeather.observeForever { openWeather ->
            persistFetchedWeather(openWeather)
        }
    }

    private fun persistFetchedWeather(fetchedWeather: OpenWeatherResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            saveLocationCoordinates(fetchedWeather.lat, fetchedWeather.lon)
            deleteCurrentWeather()
            deleteDailyWeather()
            deleteHourlyWeather()
            currentDao.upsert(fetchedWeather.current)
            for (daily in fetchedWeather.daily) {
                dailyDao.upsert(daily)
            }
            for (hourly in fetchedWeather.hourly) {
                hourlyDao.upsert(hourly)
            }
        }
    }

    suspend fun refreshWeather(unit: String): LiveData<CurrentForecast> {
        return withContext(Dispatchers.IO) {
            fetchCurrentWeather(unit)
            return@withContext currentDao.getCurrentWeatherFromID()
        }
    }

    suspend fun getCurrentWeather(unit: String): LiveData<CurrentForecast> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unit)
            return@withContext currentDao.getCurrentWeatherFromID()
        }
    }

    suspend fun getDailyForecast(unit: String): LiveData<List<DailyForecast>> {
        return withContext(Dispatchers.IO) {
            return@withContext dailyDao.getDailyForecast()
        }
    }

    suspend fun getHourlyWeather8(unit: String): LiveData<List<HourlyForecast>> {
        return withContext(Dispatchers.IO) {
            return@withContext hourlyDao.get8HourlyForecast()
        }
    }

    suspend fun getLocationName(): String {
        return withContext(Dispatchers.IO) {
            locationProvider.getPreferredLocationString()
        }
    }

    private suspend fun initWeatherData(unit: String) {
        val current = currentDao.getCurrentWeather()

        if (current == null || locationProvider.hasLocationChanged()) {
            fetchCurrentWeather(unit)
            return
        }

        if (isFetchCurrentNeeded(
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(
                        current.dt.toLong()
                    ),
                    ZoneId.systemDefault()
                )
            )
        ) {
            fetchCurrentWeather(unit)
        }

    }

    private suspend fun fetchCurrentWeather(unit: String) {
        val latLng: LatLng = locationProvider.getPreferredLocationCoordinates()
        openWeatherNetworkDataSource.fetchOpenWeather(latLng.latitude, latLng.longitude, unit)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: LocalDateTime): Boolean {
        val countDownTimer = LocalDateTime.now().minusMinutes(60)
        return lastFetchTime.isBefore(countDownTimer)
    }

    private fun deleteCurrentWeather() {
        currentDao.deleteCurrentWeather()
    }

    private fun deleteDailyWeather() {
        dailyDao.deleteDailyForecast()
    }

    private fun deleteHourlyWeather() {
        hourlyDao.deleteHourlyForecast()
    }

    private fun saveLocationCoordinates(lat: Double, lon: Double) {
        locationProvider.saveCoordinates(LatLng(lat, lon))
    }
}