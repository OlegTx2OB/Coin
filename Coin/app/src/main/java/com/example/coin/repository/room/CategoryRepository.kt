package com.example.coin.repository.room

import androidx.lifecycle.LiveData
import com.example.coin.data.Category

interface CategoryRepository {
    fun getAllCategories(): LiveData<List<Category>>

    fun insertCategory(category: Category)

    fun deleteCategory(category: Category)

}