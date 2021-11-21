package com.grigorevmp.fastweather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grigorevmp.fastweather.data.db.entity.CURRENT_WEATHER_ID
import com.grigorevmp.fastweather.data.db.entity.CurrentForecast
import com.grigorevmp.fastweather.data.db.entity.DailyForecast
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast

@Dao
interface CurrentForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weather: CurrentForecast)

    @Query("select * from current_forecast where id=$CURRENT_WEATHER_ID")
    fun getCurrentWeatherFromID(): LiveData<CurrentForecast>

    @Query("select * from current_forecast")
    fun getCurrentWeather(): CurrentForecast?

    @Query("delete from current_forecast")
    fun deleteCurrentWeather()
}

@Dao
interface DailyForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(dailyWeather: DailyForecast)

    @Query("select * from daily_forecast")
    fun getDailyForecast(): LiveData<List<DailyForecast>>

    @Query("delete from daily_forecast")
    fun deleteDailyForecast()
}


@Dao
interface HourlyForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(hourlyWeather: HourlyForecast)

    @Query("select * from hourly_forecast")
    fun getHourlyForecast(): LiveData<List<HourlyForecast>>

    @Query("delete from hourly_forecast")
    fun deleteHourlyForecast()

    @Query("select * from hourly_forecast limit 8")
    fun get8HourlyForecast(): LiveData<List<HourlyForecast>>
}