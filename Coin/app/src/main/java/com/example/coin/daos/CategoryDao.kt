package com.example.coin.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.coin.data.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert
    fun insertCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)
}