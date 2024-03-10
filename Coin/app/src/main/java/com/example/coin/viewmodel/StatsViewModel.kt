package com.example.coin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coin.data.Note
import com.example.coin.repository.room.NoteRepository
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
class StatsViewModel @Inject constructor(
    private val mNoteRepository: NoteRepository,
    private val mApp: Application
) : AndroidViewModel(mApp) {

    private val _ldExpPieData = MutableLiveData<PieData>()
    private val _ldIncPieData = MutableLiveData<PieData>()
    private val _ldExpTopCategoriesText = MutableLiveData<String>()
    private val _ldIncTopCategoriesText = MutableLiveData<String>()

    val ldExpPieData: LiveData<PieData> = _ldExpPieData
    val ldIncPieData: LiveData<PieData> = _ldIncPieData
    val ldExpTopCategoriesText: LiveData<String> = _ldExpTopCategoriesText
    val ldIncTopCategoriesText: LiveData<String> = _ldIncTopCategoriesText

    var mNotes: List<Note>? = null

    init {
        mNoteRepository.getAllNotes().observeForever {
            mNotes = it
            updateData(it)
        }
    }

    private fun updateData(notes: List<Note>?) {

        val month = spGetMonth(mApp)
        val year = spGetYear(mApp)
        val (epochDayStartPeriod, epochDayEndPeriod) = getTimeBoundariesUseCase(month, year)

        CoroutineScope(Dispatchers.Main).launch {
            val incomesNotes = notes?.filter {
                it.isIncomes == true &&
                        it.epochDay!! in epochDayStartPeriod until epochDayEndPeriod
            }
            val expensesNotes = notes?.filter {
                it.isIncomes == false &&
                        it.epochDay!! in epochDayStartPeriod until epochDayEndPeriod
            }

            var incomesPair: Pair<PieData, String>? = null
            var expensesPair: Pair<PieData, String>? = null

            val job = CoroutineScope(Dispatchers.Default).launch {
                incomesPair = updatePieChartSectionData(incomesNotes,15, mApp)
                expensesPair = updatePieChartSectionData(expensesNotes, 15, mApp)
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

}