package com.example.coin.viewmodel

import android.app.Application
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coin.R
import com.example.coin.data.Note
import com.example.coin.repository.room.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val mNoteRepository: NoteRepository,
    private val mApp: Application,
) : AndroidViewModel(mApp) {

    private val _ldGetAmount = MutableLiveData<Unit>()
    private val _ldShowToast = MutableLiveData<String>()

    val ldGetAmount: LiveData<Unit> = _ldGetAmount
    val ldShowToast: LiveData<String> = _ldShowToast


    private val mNewNote = Note()

    fun onCardViewIncExp(isIncomes: Boolean) {
        mNewNote.isIncomes = isIncomes
    }

    fun setAmount(string: String?) {
        if (string != null && string != "") mNewNote.amount = string.toFloat()
    }

    fun onAdd() {
        _ldGetAmount.value = Unit

        val todayEpochDay = LocalDate.now().toEpochDay()
        if (mNewNote.epochDay == null) {
            mNewNote.epochDay = todayEpochDay
        }

        _ldShowToast.value = if (mNewNote.amount == null) {
            mApp.getString(R.string.enter_amount)
        } else if (mNewNote.isIncomes == null) {
            mApp.getString(R.string.choose_incomes_or_outcomes)
        } else if (mNewNote.imageName == null || mNewNote.categoryName == null || mNewNote.color == null) {
            mApp.getString(R.string.choose_category)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                mNoteRepository.insertNote(mNewNote)
            }
            mApp.getString(R.string.saved)
        }
    }

    fun onCategory(view: View) {
        view as CardView
        val cardRootLayout = view.getChildAt(0) as ViewGroup

        for (i in 0 until cardRootLayout.childCount) {
            val childView = cardRootLayout.getChildAt(i)

            if (childView is CardView) {
                mNewNote.color = childView.cardBackgroundColor.defaultColor
                val imageView = childView.getChildAt(0) as ImageView
                mNewNote.imageName = imageView.tag.toString()
            } else if (childView is TextView) {
                mNewNote.categoryName = childView.text.toString()
            }
        }

    }

    fun setTodayMinusDays(daysCount: Int) {
        mNewNote.epochDay = LocalDate.now().toEpochDay() - daysCount
    }

    fun setEpochDay(daysCount: Long) {
        mNewNote.epochDay = daysCount
    }
}

//// Получаем цвет фона CardView
//val typedValue = TypedValue()
//context.theme.resolveAttribute(R.attr.cardBackgroundColor, typedValue, true)
//val colorCardView = typedValue.data
//
//// Добавляем 50% альфа-канал к цвету фона
//val alphaColor = ColorUtils.setAlphaComponent(colorCardView, (255 * 0.5).toInt())
//
//// Устанавливаем новый цвет с альфа-каналом для CardView
//cardView.setCardBackgroundColor(alphaColor)