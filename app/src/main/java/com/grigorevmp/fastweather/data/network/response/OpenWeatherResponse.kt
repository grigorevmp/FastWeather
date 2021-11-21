package com.grigorevmp.fastweather.data.network.response

import com.google.gson.annotations.SerializedName
import com.grigorevmp.fastweather.data.db.entity.CurrentForecast
import com.grigorevmp.fastweather.data.db.entity.DailyForecast
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast

data class OpenWeatherResponse(
    @SerializedName("current")
    val current: CurrentForecast,
    @SerializedName("daily")
    val daily: List<DailyForecast>,
    @SerializedName("hourly")
    val hourly: List<HourlyForecast>,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)