package com.example.coin.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.coin.R
import com.example.coin.databinding.FragmentDataboardBinding
import com.example.coin.databinding.FragmentStatsBinding
import com.example.coin.viewmodel.StatsViewModel
import com.github.mikephil.charting.charts.PieChart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : Fragment(R.layout.fragment_stats) {
    private val mVM: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentStatsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)

        setViewsPresets(binding)
        setupObservers(binding, mVM)
        setupClickListeners(binding, mVM)

        return binding.root
    }

    private fun setupClickListeners(binding: FragmentStatsBinding, mVM: StatsViewModel) {

    }

    private fun setupObservers(binding: FragmentStatsBinding, mVM: StatsViewModel) = with(binding) {
        mVM.ldExpTopCategoriesText.observe(viewLifecycleOwner) {
            expLayout.tvTopCategories.text = it
        }
        mVM.ldIncTopCategoriesText.observe(viewLifecycleOwner) {
            incLayout.tvTopCategories.text = it
        }
        mVM.ldExpPieData.observe(viewLifecycleOwner) {
            val expensesPie = expLayout.pieChart
            expensesPie.data = it
            expensesPie.notifyDataSetChanged()
            expensesPie.invalidate()
            expensesPie.visibility = View.VISIBLE

            if (it.dataSet.entryCount != 0) {
                expLayout.imageCross.visibility = View.INVISIBLE
            } else {
                expLayout.imageCross.visibility = View.VISIBLE
            }
        }
        mVM.ldIncPieData.observe(viewLifecycleOwner) {
            val incomesPie = incLayout.pieChart
            incomesPie.data = it
            incomesPie.notifyDataSetChanged()
            incomesPie.invalidate()
            incomesPie.visibility = View.VISIBLE
            if (it.dataSet.entryCount != 0) {
                incLayout.imageCross.visibility = View.INVISIBLE
            } else {
                incLayout.imageCross.visibility = View.VISIBLE
            }
        }
    }

    private fun setViewsPresets(binding: FragmentStatsBinding) = with(binding) {
        expLayout.pieChart.visibility = View.INVISIBLE
        incLayout.pieChart.visibility = View.INVISIBLE

        setPieOptions(expLayout.pieChart)
        setPieOptions(incLayout.pieChart)
    }

    private fun setPieOptions(pieChart: PieChart) {
        pieChart.holeRadius = 65f
        pieChart.description.isEnabled = false
        pieChart.animateY(400)
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.invalidate()
    }
}