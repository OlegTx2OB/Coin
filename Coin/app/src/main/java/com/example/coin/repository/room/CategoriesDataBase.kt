package com.example.coin.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coin.daos.CategoryDao
import com.example.coin.data.Category

@Database(entities = [Category::class], version = 1)
abstract class CategoriesDataBase : RoomDatabase() {
    abstract fun categoriesDao(): CategoryDao
}