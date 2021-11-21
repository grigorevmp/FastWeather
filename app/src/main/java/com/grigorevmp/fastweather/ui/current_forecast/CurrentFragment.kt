package com.grigorevmp.fastweather.ui.current_forecast

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.grigorevmp.fastweather.R
import com.grigorevmp.fastweather.common.utils.Utils
import com.grigorevmp.fastweather.data.db.entity.HourlyForecast
import com.grigorevmp.fastweather.databinding.NavigationCurrentForecastFragmentBinding
import com.grigorevmp.fastweather.ui.recycler_view.hour.HourForecastAdapter
import kotlinx.coroutines.*

class CurrentFragment : Fragment() {
    private var _binding: NavigationCurrentForecastFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CurrentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NavigationCurrentForecastFragmentBinding.inflate(inflater, container, false)
        initViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.srlOpenSwipeRefresh.isRefreshing = true
        if (context?.let { Utils.hasLocationPermission(it) } == true)
            refreshUI(false)
        else {
            binding.cvMainCard.visibility = View.GONE
            binding.llAdditional.visibility = View.GONE
            binding.hsvItems.visibility = View.GONE
            binding.cvAskPermission.visibility = View.VISIBLE
            binding.srlOpenSwipeRefresh.isRefreshing = false
        }
        binding.srlOpenSwipeRefresh.setOnRefreshListener {
            refreshUI(true)
        }
        setListeners()
    }

    private fun setListeners() {
        binding.bAskPermission.setOnClickListener {
            if (context?.let { Utils.hasLocationPermission(it) } == true) {
                Utils.makeToast(context!!, "It's ok")
                refreshUI(false)
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            Utils.ACCESS_LOCATION_PERMISSION
        )
    }

    private fun refreshUI(refreshWeather: Boolean) {
        if (context?.let { Utils.hasLocationPermission(it) } == true) {
            binding.cvMainCard.visibility = View.VISIBLE
            binding.llAdditional.visibility = View.VISIBLE
            binding.hsvItems.visibility = View.VISIBLE
            binding.cvAskPermission.visibility = View.GONE
        }

        lifecycleScope.launchWhenStarted {
            try {
                val currentWeather = if (refreshWeather)
                    viewModel.refreshWeather()
                else
                    viewModel.getWeather()
                withContext(Dispatchers.Main) {
                    currentWeather.observe(viewLifecycleOwner, Observer {
                        binding.srlOpenSwipeRefresh.isRefreshing = false
                        if (it == null)
                            return@Observer
                        setTemperatures(it.temp, it.feelsLike)
                        setCondition(it.weather[0].description)
                        setHumidity(it.windSpeed)
                        setClouds(it.clouds)
                        setWindSpeed(it.windSpeed)
                        val imageLink =
                            "https://openweathermap.org/img/wn/${it.weather[0].icon}@4x.png"
                        Glide.with(this@CurrentFragment)
                            .load(imageLink)
                            .placeholder(R.drawable.ic_sunny)
                            .into(binding.ivCurrentWeatherIcon)
                    })
                    val locationName = viewModel.getLocationName()
                    val hourlyWeather = viewModel.getHourlyWeather8()
                    setCity(locationName)
                    hourlyWeather.observe(viewLifecycleOwner, Observer {
                        if (it == null) return@Observer
                        loadHourlyRecycler(it)
                    })
                }
            } catch (e: Exception) {
                Log.d("REFRESH", "Fragment changed")
            } finally {
                Log.d("REFRESH", "Refresh ended")
            }
        }
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun loadHourlyRecycler(list: List<HourlyForecast>) {
        val hourlyForecastAdapter =
            HourForecastAdapter(list, viewModel.unitSystem, requireContext())
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHoursRecycler.layoutManager = layoutManager
        binding.rvHoursRecycler.adapter = hourlyForecastAdapter
    }

    private fun setCity(locationName: String) {
        binding.tvCity.text = locationName
    }

    @SuppressLint("SetTextI18n")
    private fun setTemperatures(temperature: Double, feelsLike: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        binding.tvTemperature.text = "${temperature.toInt()}$unitAbbreviation"
        binding.tvFeelsLike.text = "Feels like ${feelsLike.toInt()}$unitAbbreviation"
    }

    @SuppressLint("SetTextI18n")
    private fun setCondition(condition: String) {
        binding.tvCondition.text = condition[0].uppercaseChar() + condition.substring(1)
    }

    @SuppressLint("SetTextI18n")
    private fun setHumidity(humidity: Double) {
        binding.tvHumidity.text = "$humidity%"
    }

    @SuppressLint("SetTextI18n")
    private fun setWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("m/s", "mph")
        binding.tvWind.text = "$windSpeed $unitAbbreviation"
    }

    @SuppressLint("SetTextI18n")
    private fun setClouds(clouds: Int) {
        binding.tvClouds.text = "$clouds%"
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            CurrentViewModelFactory(requireActivity().application)
        )[CurrentViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}