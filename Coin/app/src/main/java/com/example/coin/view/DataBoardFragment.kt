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
import com.example.coin.usecase.ShowChoosePeriodDialogUseCase
import com.example.coin.viewmodel.DataBoardViewModel
import com.github.mikephil.charting.charts.PieChart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataBoardFragment : Fragment(R.layout.fragment_databoard) {
    private val mVM: DataBoardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDataboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_databoard, container, false)

        setViewsPresets(binding)
        setupObservers(binding, mVM)
        setupClickListeners(binding, mVM)

        return binding.root
    }

    private fun setViewsPresets(binding: FragmentDataboardBinding) = with(binding) {
        expLayout.pieChart.visibility = View.INVISIBLE
        incLayout.pieChart.visibility = View.INVISIBLE

        setPieOptions(expLayout.pieChart)
        setPieOptions(incLayout.pieChart)
    }

    private fun setupClickListeners(
        binding: FragmentDataboardBinding,
        mVM: DataBoardViewModel
    ) {
        binding.topSectionLayout.cardviewChoosePeriod.setOnClickListener {
            val showChoosePeriodDialogUseCase = ShowChoosePeriodDialogUseCase(
                layoutInflater.inflate(
                    R.layout.dialog_fragment_choose_period,
                    null
                ), requireContext()
            )
            showChoosePeriodDialogUseCase.setDialogListener(object : ShowChoosePeriodDialogUseCase.DialogListener {
                override fun onDialogPositiveClick() {
                    mVM.updateData(mVM.mNotes)
                }
            })
            showChoosePeriodDialogUseCase.show()
        }
    }

    private fun setupObservers(binding: FragmentDataboardBinding, mVM: DataBoardViewModel) = with(binding) {

        mVM.ldSetTimePeriod.observe(viewLifecycleOwner) {
            topSectionLayout.periodText.text = it
        }
        mVM.ldSetIncBalance.observe(viewLifecycleOwner) {
            topSectionLayout.tvIncomesValue.text = it
        }
        mVM.ldSetExpBalance.observe(viewLifecycleOwner) {
            topSectionLayout.tvExpensesValue.text = it
        }
        mVM.ldSetTotalBalance.observe(viewLifecycleOwner) {
            topSectionLayout.tvTotalBalanceValue.text = it
        }
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

    private fun setPieOptions(pieChart: PieChart) {
        pieChart.holeRadius = 65f
        pieChart.description.isEnabled = false
        pieChart.animateY(400)
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleAlpha(50)
        pieChart.invalidate()
    }

}