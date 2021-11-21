package com.grigorevmp.fastweather

import android.app.Application
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.data.db.dao.CurrentForecastDao
import com.grigorevmp.fastweather.data.db.dao.DailyForecastDao
import com.grigorevmp.fastweather.data.db.dao.HourlyForecastDao
import com.grigorevmp.fastweather.data.db.database.ForecastDatabase
import com.grigorevmp.fastweather.data.db.entity.CurrentForecast
import com.grigorevmp.fastweather.data.repository.OpenWeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import com.grigorevmp.fastweather.ui.current_forecast.CurrentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RunWith(AndroidJUnit4::class)
class CurrentFragmentViewModelTest {
    val application = androidx.test.core.app.ApplicationProvider.getApplicationContext() as Application

    @Test
    @Throws(IOException::class)
    fun getFromViewModelGetWeather() = runBlocking {
        withContext(Dispatchers.Main) {
            val currentViewModel = CurrentViewModel(application)
            val currentWeather = currentViewModel.getWeather().value
            if (currentWeather != null) {
                Assert.assertEquals(currentWeather.id, 0)
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun getFromViewModelGetHourlyWeather8() = runBlocking {
        withContext(Dispatchers.Main) {
            val currentViewModel = CurrentViewModel(application)
            val hourlyWeather8 = currentViewModel.getHourlyWeather8().value
            if (hourlyWeather8 != null) {
                Assert.assertEquals(hourlyWeather8.size, 8)
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun getFromViewModelGetLocationName() = runBlocking {
        withContext(Dispatchers.Main) {
            val currentViewModel = CurrentViewModel(application)
            val locationName = currentViewModel.getLocationName()
            Assert.assertNotEquals(locationName, null)
        }
    }
}
