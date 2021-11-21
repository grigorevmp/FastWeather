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

@RunWith(AndroidJUnit4::class)
class DataBaseTest {

    private val database = ForecastDatabase.getInstance()
    private val currentDao = database.currentDao()
    private val dailyDao = database.dailyDao()
    private val hourlyDao = database.hourlyDao()

    @Test
    @Throws(IOException::class)
    fun getFromDatabase() = runBlocking {
        var currentWeather: CurrentForecast? = currentDao.getCurrentWeather()
        if (currentWeather != null)
            Assert.assertEquals(currentWeather.id, 0)
    }

    @Test
    @Throws(IOException::class)
    fun geByIdFromDatabase() = runBlocking {
        var currentWeather: CurrentForecast? = currentDao.getCurrentWeather()
        if (currentWeather != null)
            Assert.assertEquals(currentWeather.id, 0)
        currentWeather = currentDao.getCurrentWeatherFromID().value
        if (currentWeather != null)
            Assert.assertEquals(currentWeather.id, 0)
    }

    @Test
    @Throws(IOException::class)
    fun deleteFromDatabase() = runBlocking {
        var currentWeather: CurrentForecast? = currentDao.getCurrentWeather()
        if (currentWeather != null)
            Assert.assertEquals(currentWeather.id, 0)
        currentDao.deleteCurrentWeather()
        currentWeather = currentDao.getCurrentWeatherFromID().value
        Assert.assertEquals(currentWeather, null)
    }
}
