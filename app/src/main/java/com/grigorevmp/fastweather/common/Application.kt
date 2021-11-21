package com.grigorevmp.fastweather.common

import android.app.Application
import android.os.StrictMode
import androidx.viewbinding.BuildConfig
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.data.db.database.ForecastDatabase

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        ForecastDatabase.setInstance(this)
        Utils.init(this)

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    //.penaltyDeath()
                    .build()
            )
        }
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}
