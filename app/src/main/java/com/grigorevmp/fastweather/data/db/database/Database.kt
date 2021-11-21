package com.grigorevmp.fastweather.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grigorevmp.fastweather.data.db.dao.CurrentForecastDao
import com.grigorevmp.fastweather.data.db.dao.DailyForecastDao
import com.grigorevmp.fastweather.data.db.dao.HourlyForecastDao
import com.grigorevmp.fastweather.data.db.entity.CurrentForecast
import com.grigorevmp.fastweather.data.db.entity.DailyForecast
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast
import com.grigorevmp.fastweather.data.db.utils.WeatherTypeConvertor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CurrentForecast::class, DailyForecast::class, HourlyForecast::class],
    version = 1
)
@TypeConverters(WeatherTypeConvertor::class)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun currentDao(): CurrentForecastDao
    abstract fun dailyDao(): DailyForecastDao
    abstract fun hourlyDao(): HourlyForecastDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null

        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java, "weather_database.db"
            ).build()

        fun setInstance(context: Context): ForecastDatabase? {
            if (instance == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ForecastDatabase::class.java, "weather_database.db"
                        ).build()
                    }
                }
            }
            return instance
        }

        @Synchronized
        fun getInstance(): ForecastDatabase {
            return instance!!
        }
    }
}