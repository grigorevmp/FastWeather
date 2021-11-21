package com.grigorevmp.fastweather.ui.recycler_view.hour

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.utils.UnitSystem
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast

class HourForecastAdapter(
    val list: List<HourlyForecast>,
    private val unitSystem: UnitSystem,
    val context: Context
) : RecyclerView.Adapter<HourForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourForecastViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.weather_hour_item,
            parent,
            false
        )

        return HourForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourForecastViewHolder, position: Int) {
        holder.bind(list[position], context, unitSystem)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

