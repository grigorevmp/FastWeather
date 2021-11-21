package com.grigorevmp.fastweather

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.grigorevmp.fastweather.data.db.dao.CurrentForecastDao
import com.grigorevmp.fastweather.data.db.dao.DailyForecastDao
import com.grigorevmp.fastweather.data.db.dao.HourlyForecastDao
import com.grigorevmp.fastweather.data.db.database.ForecastDatabase
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

    private lateinit var currentDao: CurrentForecastDao
    private lateinit var dailyDao: DailyForecastDao
    private lateinit var hourlyDao: HourlyForecastDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val db = Room.inMemoryDatabaseBuilder(context, ForecastDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // val repo = OpenWeatherRepository(context)
    }

    @After
    @Throws(IOException::class)
    fun deleteDb() {
        //db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetTodo() = runBlocking {
        Assert.assertEquals(1L, 1L)
    }
}