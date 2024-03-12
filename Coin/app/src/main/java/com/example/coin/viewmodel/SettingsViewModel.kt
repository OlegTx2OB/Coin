package com.example.coin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coin.R
import com.example.coin.repository.sharedprefs.spSaveCurrencyName

class SettingsViewModel (private val mApp: Application
) : AndroidViewModel(mApp) {

    private val _ldShowToast = MutableLiveData<String>()

    val ldShowToast: LiveData<String> = _ldShowToast
    fun onSaveCurrencyName(currencyName: String?) {
        _ldShowToast.value = if (currencyName != null && currencyName != "") {
            spSaveCurrencyName(currencyName, mApp)
            mApp.getString(R.string.saved)
        } else {
            mApp.getString(R.string.enter_value)
        }
    }
}