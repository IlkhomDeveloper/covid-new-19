package com.example.covid19.presenter.screen.main

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.covid19.R
import com.example.covid19.databinding.FragmentMainPageBinding
import com.example.covid19.presenter.model.CovidTotal
import com.example.covid19.presenter.model.DataFromTo
import com.example.covid19.presenter.utils.HelperFunctions
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

@AndroidEntryPoint
class MainPage : Fragment(R.layout.fragment_main_page) {
    private val viewModel: MainPageViewModel by viewModels()
    private val handler = Handler()
    private var runnableCode: Runnable ?= null
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainPageBinding.bind(view)

        onClick()

        viewModelsAction()

        repeatEveryPeriodTime()
    }

    private fun repeatEveryPeriodTime(){
       runnableCode  = object : Runnable {
            override fun run() {
                viewModel.getUzbSingle()
                viewModel.getWorldTotal()
                handler.postDelayed(this, 10000)
            }
        }

        handler.post(runnableCode!!)
    }

    private fun viewModelsAction() {
        viewModel.getWorldTotal()

        viewModel.getUzbSingle()

        viewModel.worldTotal.observe(viewLifecycleOwner, observerWorldTotal)

        viewModel.message.observe(viewLifecycleOwner, observerMessage)

        viewModel.covidUzb.observe(viewLifecycleOwner, observerUzbTotal)

    }

    private fun onClick() {
        binding.refresh.setOnClickListener { }
        binding.from.setOnClickListener { HelperFunctions.showDateDialog(requireContext(), binding.from) }
        binding.to.setOnClickListener { HelperFunctions.showDateDialog(requireContext(), binding.to) }
        binding.downLoad.setOnClickListener { sendDate() }
        binding.refresh.setOnClickListener {
            viewModel.getWorldTotal()
            viewModel.getUzbSingle()
        }
    }

    private fun sendDate() {
        val from = binding.from.text.toString() + "T00:00:00Z"
        val to = binding.to.text.toString() + "T00:00:00Z"
        viewModel.getUzbTotal(DataFromTo(from, to))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setPieChart(pieChart: PieChart, covidTotal: CovidTotal) {
        pieChart.clearChart()
        pieChart.addPieSlice(
            PieModel(
                covidTotal.TotalConfirmed.toFloat(),
                Color.YELLOW
            )
        )
        pieChart.addPieSlice(
            PieModel(
                (covidTotal.TotalRecovered - covidTotal.TotalDeaths).toFloat(),
                Color.GREEN
            )
        )
        pieChart.addPieSlice(
            PieModel(
                covidTotal.TotalDeaths.toFloat(),
                Color.RED
            )
        )
        pieChart.startAnimation()
    }

    private fun setCountUzb(covidTotal: CovidTotal) {
        binding.uzbPositive.text = covidTotal.TotalRecovered.toString()
        binding.uzbNegative.text = covidTotal.TotalConfirmed.toString()
        binding.uzbDied.text = covidTotal.TotalDeaths.toString()
    }

    private fun setCountWorld(covidTotal: CovidTotal) {
        binding.worldPositive.text = covidTotal.TotalRecovered.toString()
        binding.worldNegative.text = covidTotal.TotalConfirmed.toString()
        binding.worldDied.text = covidTotal.TotalDeaths.toString()
    }

    private val observerWorldTotal = Observer<CovidTotal> {
        setPieChart(binding.worldChart, it)
        setCountWorld(it)
    }

    private val observerUzbTotal = Observer<CovidTotal> {
        setPieChart(binding.uzbChart, it)
        setCountUzb(it)
    }

    private val observerMessage = Observer<String> { showMessage(it) }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnableCode!!)
    }
}