package com.grigorevmp.fastweather.ui.recycler_view.day

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
import com.grigorevmp.fastweather.common.utils.Utils.getDateFromUTC
import com.grigorevmp.fastweather.data.db.entity.DailyForecast

class DailyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvDayIcon = itemView.findViewById<ImageView>(R.id.tvDayIcon)
    private val tvDayTemp = itemView.findViewById<TextView>(R.id.tvDayTemp)
    private val tvDayWind = itemView.findViewById<TextView>(R.id.tvDayWind)
    private val tvDayDate = itemView.findViewById<TextView>(R.id.tvDayDate)
    private val tvDayCondition = itemView.findViewById<TextView>(R.id.tvDayCondition)
    private val tvDayClouds = itemView.findViewById<TextView>(R.id.tvDayClouds)
    private val tvDayRain = itemView.findViewById<TextView>(R.id.tvDayRain)

    @SuppressLint("SetTextI18n")
    fun bindView(daily: DailyForecast, context: Context, unitSystem: UnitSystem) {
        val condition = daily.weather[0].description
        tvDayCondition.text = condition[0].uppercaseChar() + condition.substring(1)
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${daily.weather[0].icon}@4x.png")
            .into(tvDayIcon)
        tvDayTemp.text = "Max: ${daily.temp.max.toInt()} " +
                chooseLocalizedUnitAbbreviation(unitSystem, "°C", "°F") +
                " Min: ${daily.temp.min.toInt()} " +
                chooseLocalizedUnitAbbreviation(unitSystem, "°C", "°F")
        tvDayWind.text = "Wind: ${daily.windSpeed} ${
            chooseLocalizedUnitAbbreviation(
                unitSystem,
                "m/s",
                "mph"
            )
        }"
        tvDayDate.text = getDateFromUTC(daily.dt)
        tvDayClouds.text = "Clouds: ${daily.clouds}%"
        tvDayRain.text = "${(daily.pop * 100).toInt()}%"
    }
}