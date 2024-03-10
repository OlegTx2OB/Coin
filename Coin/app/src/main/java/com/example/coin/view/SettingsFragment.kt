package com.example.coin.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.coin.R
import com.example.coin.databinding.FragmentSettingsBinding
import com.example.coin.repository.sharedprefs.spGetCurrencyName
import com.example.coin.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val mVM: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        setViewsPresets(binding)
        setupObservers(binding, mVM)
        setupClickListeners(binding, mVM)

        return binding.root
    }

    private fun setViewsPresets(binding: FragmentSettingsBinding) {
        binding.cardviewSetCurrencyName.textInputLayout.hint = getString(R.string.currency_name)
        binding.cardviewSetCurrencyName.textInputEditText.text =
            Editable.Factory.getInstance().newEditable(spGetCurrencyName(requireContext()))
    }

    private fun setupObservers(binding: FragmentSettingsBinding, mVM: SettingsViewModel) {
        mVM.ldShowToast.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners(binding: FragmentSettingsBinding, mVM: SettingsViewModel) {
        binding.cardviewSetCurrencyName.btnSave.setOnClickListener {
            mVM.onSaveCurrencyName(binding.cardviewSetCurrencyName.textInputEditText.text?.toString())
        }
    }
}