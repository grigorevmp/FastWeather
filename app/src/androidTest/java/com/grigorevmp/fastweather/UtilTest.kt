package com.grigorevmp.fastweather

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grigorevmp.fastweather.ui.future_forecast.FutureViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.common.utils.UnitSystem

@RunWith(AndroidJUnit4::class)
class UtilTest {
    val application = androidx.test.core.app.ApplicationProvider.getApplicationContext() as Application

    @Before
    fun init(){
        AndroidThreeTen.init(application)
        Utils.init(application)
    }

    @Test
    @Throws(IOException::class)
    fun getTimeFromUTC() = runBlocking {
        val data = "${Utils.getTimeFromUTC(1637514000)}"
        Assert.assertEquals(data, "20:00")
    }

    @Test
    @Throws(IOException::class)
    fun getDateFromUTC() = runBlocking {
        val data = "${Utils.getDateFromUTC(1637485200)}"
        Assert.assertEquals(data, "21/11/2021")
    }

    @Test
    @Throws(IOException::class)
    fun chooseLocalizedUnitAbbreviation() = runBlocking {
        val data = Utils.chooseLocalizedUnitAbbreviation(
            UnitSystem.metric,
            "m/s",
            "mph"
        )
        Assert.assertEquals(data, "m/s")
        val data2 = Utils.chooseLocalizedUnitAbbreviation(
            UnitSystem.imperial,
            "m/s",
            "mph"
        )
        Assert.assertEquals(data2, "mph")
    }
}