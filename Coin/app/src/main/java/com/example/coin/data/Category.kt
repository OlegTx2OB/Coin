package com.example.coin.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var text: String? = null,
    var imageName: String? = null,
    var color: Int? = null
)