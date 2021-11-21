package com.grigorevmp.fastweather.data.db.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grigorevmp.fastweather.data.network.response.Forecast
import java.lang.reflect.Type
import java.util.*

class WeatherTypeConvertor {
    private val gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Forecast> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Forecast>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<Forecast>): String {
        return gson.toJson(someObjects)
    }
}