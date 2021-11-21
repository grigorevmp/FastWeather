package com.grigorevmp.fastweather.ui.recycler_view.hour

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.utils.UnitSystem
import com.grigorevmp.fastweather.common.utils.Utils.chooseLocalizedUnitAbbreviation
import com.grigorevmp.fastweather.common.utils.Utils.getTimeFromUTC
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast

class HourForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val hourlyWeatherIcon = itemView.findViewById<ImageView>(R.id.ivHourIcon)
    private val hourlyTemp = itemView.findViewById<TextView>(R.id.tvHourTemperature)
    private val hourlyTime = itemView.findViewById<TextView>(R.id.tvHourTime)
    private val hourlyPrecipitationProbability = itemView.findViewById<TextView>(R.id.tvHourWeather)

    @SuppressLint("SetTextI18n")
    fun bind(hourly: HourlyForecast, context: Context, unitSystem: UnitSystem) {
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${hourly.weather[0].icon}@2x.png")
            .placeholder(R.drawable.ic_sunny)
            .into(hourlyWeatherIcon)
        hourlyTemp.text =
            "${hourly.temp.toInt()} ${chooseLocalizedUnitAbbreviation(unitSystem, "°C", "°F")}"
        hourlyTime.text = getTimeFromUTC(hourly.dt)
        hourlyPrecipitationProbability.text = "${(hourly.pop * 100).toInt()}%"
    }
}