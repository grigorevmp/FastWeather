package com.grigorevmp.fastweather.ui.future_forecast

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.data.db.entity.DailyForecast
import com.grigorevmp.fastweather.data.repository.OpenWeatherRepository


class FutureViewModel(
    application: Application,
) : ViewModel() {

    val unitSystem = Utils.unitSystem()
    private val openWeatherRepository = OpenWeatherRepository(application)

    suspend fun getDailyForecast(): LiveData<List<DailyForecast>> {
        return openWeatherRepository.getDailyForecast(unitSystem.name)
    }

}