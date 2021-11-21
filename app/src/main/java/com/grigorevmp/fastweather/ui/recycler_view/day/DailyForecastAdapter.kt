package com.grigorevmp.fastweather.ui.recycler_view.day

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.utils.UnitSystem
import com.grigorevmp.fastweather.data.db.entity.DailyForecast

class DailyForecastAdapter(
    val list: List<DailyForecast>,
    private val unitSystem: UnitSystem,
    val context: Context
) : RecyclerView.Adapter<DailyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.weather_day_item, parent,
            false
        )
        return DailyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bindView(list[position], context, unitSystem)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}