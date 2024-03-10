package com.example.coin.repository.room

import androidx.lifecycle.LiveData
import com.example.coin.daos.CategoryDao
import com.example.coin.data.Category
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override fun getAllCategories(): LiveData<List<Category>> {
        return categoryDao.getAllCategories()
    }

    override fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}