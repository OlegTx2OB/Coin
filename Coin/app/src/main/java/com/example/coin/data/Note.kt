package com.example.coin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var categoryName: String? = null,
    var isIncomes: Boolean? = null,
    var amount: Float? = null,
    var imageName: String? = null,
    var epochDay: Long? = null,
    var color: Int? = null
)