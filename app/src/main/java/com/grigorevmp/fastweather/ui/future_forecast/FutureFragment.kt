package com.grigorevmp.fastweather.ui.future_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigorevmp.fastweather.data.db.entity.DailyForecast
import com.grigorevmp.fastweather.databinding.NavigationFutureForecastFragmentBinding
import com.grigorevmp.fastweather.ui.recycler_view.day.DailyForecastAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FutureFragment : Fragment() {

    private var _binding: NavigationFutureForecastFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FutureViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NavigationFutureForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        refreshUI()
    }

    private fun refreshUI() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dailyForecast = viewModel.getDailyForecast()

            withContext(Dispatchers.Main) {
                dailyForecast.observe(viewLifecycleOwner, Observer {
                    if (it == null)
                        return@Observer

                    loadDailyRecycler(it)
                })
            }
        }
    }

    private fun loadDailyRecycler(list: List<DailyForecast>) {
        val dailyWeatherAdapter = DailyForecastAdapter(list, viewModel.unitSystem, requireContext())
        val layoutManager = LinearLayoutManager(context)
        binding.rvWeekForecast.layoutManager = layoutManager
        binding.rvWeekForecast.adapter = dailyWeatherAdapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            FutureViewModelFactory(requireActivity().application)
        )[FutureViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}