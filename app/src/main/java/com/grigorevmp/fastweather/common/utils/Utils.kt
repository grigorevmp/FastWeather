package com.grigorevmp.fastweather.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    const val ACCESS_LOCATION_PERMISSION = 1

    private var application: Application? = null


    fun getString(@StringRes id: Int, vararg parameters: Any): String {
        return application?.getString(id, parameters)
            ?: throw IllegalStateException(
                "Application context in Utils not initialized.Please " +
                        "call method init in your Application instance"
            )
    }

    fun chooseLocalizedUnitAbbreviation(
        unitSystem: UnitSystem,
        metric: String,
        imperial: String
    ): String {
        return if (unitSystem.name == UnitSystem.metric.name) metric else imperial
    }

    fun getDateFromUTC(time: Int): String {
        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        val date = Date(time.toLong() * 1000)
        val fmt = SimpleDateFormat(("dd/MM/yyyy"), Locale.US)
        fmt.timeZone = tz
        return fmt.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeFromUTC(time: Int): String {
        val cal = Calendar.getInstance()
        val tz = cal.timeZone
        val date = Date(time.toLong() * 1000)
        val fmt = SimpleDateFormat(("HH:mm"))
        fmt.timeZone = tz
        return fmt.format(date)
    }

    fun unitSystem(): UnitSystem {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            application?.applicationContext
        )
        val selectedName =
            preferences.getString("UNIT_SYSTEM", UnitSystem.metric.name) ?: UnitSystem.imperial.name
        return UnitSystem.valueOf(selectedName)
    }

    fun makeToast(context: Context, string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    fun init(application: Application) {
        Utils.application = application
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

enum class UnitSystem {
    metric, imperial
}
