package com.grigorevmp.fastweather

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grigorevmp.fastweather.data.db.database.ForecastDatabase
import com.grigorevmp.fastweather.ui.future_forecast.FutureViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FutureFragmentViewModelTest {
    val application = androidx.test.core.app.ApplicationProvider.getApplicationContext() as Application

    @Test
    @Throws(IOException::class)
    fun getFromViewModelGetDailyForecast() = runBlocking {
        AndroidThreeTen.init(application)
        withContext(Dispatchers.Main) {
            val currentViewModel = FutureViewModel(application)
            val currentWeather = currentViewModel.getDailyForecast().value
            if (currentWeather != null) {
                Assert.assertEquals(currentWeather.size, 7)
            }
        }
    }
}