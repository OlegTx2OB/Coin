package com.example.coin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coin.data.Note
import com.example.coin.repository.room.NoteRepository
import com.example.coin.repository.sharedprefs.spGetCurrencyName
import com.example.coin.repository.sharedprefs.spGetMonth
import com.example.coin.repository.sharedprefs.spGetYear
import com.example.coin.usecase.getTimeBoundariesUseCase
import com.example.coin.usecase.updatePieChartSectionData
import com.github.mikephil.charting.data.PieData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataBoardViewModel @Inject constructor(
    private val mNoteRepository: NoteRepository, private val mApp: Application
) : AndroidViewModel(mApp) {

    // Exp = Expenses, Inc = Incomes
    private val _ldExpPieData = MutableLiveData<PieData>()
    private val _ldIncPieData = MutableLiveData<PieData>()
    private val _ldExpTopCategoriesText = MutableLiveData<String>()
    private val _ldIncTopCategoriesText = MutableLiveData<String>()
    private val _ldSetExpBalance = MutableLiveData<String>()
    private val _ldSetIncBalance = MutableLiveData<String>()
    private val _ldSetTotalBalance = MutableLiveData<String>()
    private val _ldSetTimePeriod = MutableLiveData<String>()

    val ldExpPieData: LiveData<PieData> = _ldExpPieData
    val ldIncPieData: LiveData<PieData> = _ldIncPieData
    val ldExpTopCategoriesText: LiveData<String> = _ldExpTopCategoriesText
    val ldIncTopCategoriesText: LiveData<String> = _ldIncTopCategoriesText
    val ldSetExpBalance: LiveData<String> = _ldSetExpBalance
    val ldSetIncBalance: LiveData<String> = _ldSetIncBalance
    val ldSetTotalBalance: LiveData<String> = _ldSetTotalBalance
    val ldSetTimePeriod: LiveData<String> = _ldSetTimePeriod


    var mNotes: List<Note>? = null

    init {
        mNoteRepository.getAllNotes().observeForever {
            mNotes = it
            updateData(it)
        }
    }

    fun updateData(notes: List<Note>?) {

        val month = spGetMonth(mApp)
        val year = spGetYear(mApp)
        val (epochDayStartPeriod, epochDayEndPeriod) = getTimeBoundariesUseCase(month, year)
        setPeriodText(month, year)


        CoroutineScope(Dispatchers.Main).launch {
            val incomesNotes = notes?.filter {
                it.isIncomes == true &&
                        it.epochDay!! in epochDayStartPeriod until epochDayEndPeriod
            }
            val expensesNotes = notes?.filter {
                it.isIncomes == false &&
                        it.epochDay!! in epochDayStartPeriod until epochDayEndPeriod
            }

            CoroutineScope(Dispatchers.Main).launch {
                setBalances(incomesNotes, expensesNotes)
            }

            var incomesPair: Pair<PieData, String>? = null
            var expensesPair: Pair<PieData, String>? = null


            val job = CoroutineScope(Dispatchers.Default).launch {
                incomesPair = updatePieChartSectionData(incomesNotes, 4, mApp)
                expensesPair = updatePieChartSectionData(expensesNotes, 4, mApp)
            }

            CoroutineScope(Dispatchers.Main).launch {
                job.join()
                _ldIncPieData.value = incomesPair!!.first
                _ldIncTopCategoriesText.value = incomesPair!!.second
                _ldExpPieData.value = expensesPair!!.first
                _ldExpTopCategoriesText.value = expensesPair!!.second
            }
        }

    }
    private fun setPeriodText(month: Int, year: Int) {
        //month == 0 means that month is not chosen
        _ldSetTimePeriod.value = if (month == 0) {
            "$year"
        } else {
            "$month.$year"
        }
    }



    private fun setBalances(incomesNotes: List<Note>?, expensesNotes: List<Note>?) {
        var incomesBalance = 0f
        var expensesBalance = 0f
        if (incomesNotes != null) {
            for (note in incomesNotes) incomesBalance += note.amount!!
            _ldSetIncBalance.value = incomesBalance.toString()
        }
        if (expensesNotes != null) {
            for (note in expensesNotes) expensesBalance += note.amount!!
            _ldSetExpBalance.value = expensesBalance.toString()
        }
        _ldSetTotalBalance.value =
            (incomesBalance - expensesBalance).toString() + spGetCurrencyName(mApp)
    }

}