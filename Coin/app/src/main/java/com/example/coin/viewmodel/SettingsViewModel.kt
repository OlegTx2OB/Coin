package com.example.coin.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coin.R
import com.example.coin.repository.room.NoteRepository
import com.example.coin.repository.sharedprefs.spSaveCurrencyName
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mNoteRepository: NoteRepository, private val mApp: Application
) : AndroidViewModel(mApp)
{

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