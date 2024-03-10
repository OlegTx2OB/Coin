package com.example.coin.viewmodel

import android.app.Application
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coin.data.Category
import com.example.coin.repository.room.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val mCategoryRepository: CategoryRepository,
    private val mApp: Application,
) : AndroidViewModel(mApp) {

    private val mNewCategory = Category()

    private val _ldGetAmount = MutableLiveData<Unit>()
    val ldGetAmount: LiveData<Unit> = _ldGetAmount

    fun setAmount(string: String?) {
        if (string != null && string != "") mNewCategory.text = string
    }

    fun onAddCardviewAndReturnResult(): Pair<Boolean, String> {
        _ldGetAmount.value = Unit
        return if (mNewCategory.text == null || mNewCategory.text == "") {
            false to "enter category name"
        } else if (mNewCategory.color == null) {
            false to "choose color"
        } else if (mNewCategory.imageName == null) {
            false to "choose category"
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                mCategoryRepository.insertCategory(mNewCategory)
            }
            true to "all saved"
        }
    }

    fun onSetCategoryColor(cardView: CardView) {
        mNewCategory.color = cardView.cardBackgroundColor.defaultColor
    }

    fun onSetCategory(cardView: CardView) {
        val imageView = cardView.getChildAt(0) as ImageView
        mNewCategory.imageName = imageView.tag.toString()
    }


}