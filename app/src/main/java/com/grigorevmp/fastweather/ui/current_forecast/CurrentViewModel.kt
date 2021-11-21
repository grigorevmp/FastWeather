package com.grigorevmp.fastweather.ui.current_forecast

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.grigorevmp.fastweather.common.utils.UnitSystem
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.data.db.entity.CurrentForecast
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast
import com.grigorevmp.fastweather.data.repository.OpenWeatherRepository


class CurrentViewModel(
    application: Application,
) : ViewModel() {

    val unitSystem = Utils.unitSystem()
    private val openWeatherRepository = OpenWeatherRepository(application)

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.metric

    suspend fun getWeather(): LiveData<CurrentForecast> {
        return openWeatherRepository.getCurrentWeather(unitSystem.name)
    }

    suspend fun getHourlyWeather8(): LiveData<List<HourlyForecast>> {
        return openWeatherRepository.getHourlyWeather8(unitSystem.name)
    }

    suspend fun getLocationName(): String {
        return openWeatherRepository.getLocationName()
    }

    suspend fun refreshWeather(): LiveData<CurrentForecast> {
        return openWeatherRepository.refreshWeather(unitSystem.name)
    }

}